import { Component, OnInit, TrackByFunction } from "@angular/core";
import { Observable, take, takeUntil } from "rxjs";
import { CartItem } from "../../shopping-cart/model/cart-item.model";
import { Address } from "../../account-setting/model/address.model";
import { FormBuilder } from "@angular/forms";
import { BuyNowCartRepository } from "../../shopping-cart/repository/buy-now-cart.repository";
import { ShoppingCartService } from "../../shopping-cart/repository/shopping-cart.service";
import { OrderService } from "../../account-setting/repository/order/order.service";
import { AddressRepository } from "../../account-setting/repository/address/address.repository";
import { AddressService } from "../../account-setting/repository/address/address.service";
import { UnsubscribeComponent } from "../../../core/component/unsubscribe/unsubscribe.component";

@Component({
  selector: "app-buy-now",
  templateUrl: "./buy-now.component.html",
  styleUrls: ["./buy-now.component.scss"],
})
export class BuyNowComponent extends UnsubscribeComponent implements OnInit {

  cartItemCount$: Observable<number> = this.buyNowCartRepository.cartItemCount$;
  cartItems$: Observable<Array<CartItem>> = this.buyNowCartRepository.cartItems$;
  selectedAddress!: Address;
  addresses$: Observable<Array<Address>> = this.addressRepository.addresses$;
  buyNowProductId: string = "";

  constructor(
    private formBuilder: FormBuilder,
    private buyNowCartRepository: BuyNowCartRepository,
    private shoppingCartService: ShoppingCartService,
    private orderService: OrderService,
    private addressRepository: AddressRepository,
    private addressService: AddressService,
  ) {
    super();
  }

  ngOnInit(): void {
    this.buyNowCartRepository.cartItems$.pipe(take(1)).subscribe(cartItems => {
      if (cartItems.length > 0) {
        this.buyNowProductId = cartItems[0].productId;
      }
    });

    this.addressService.getPage();
    this.addressRepository.addresses$.pipe(takeUntil(this.unsubscribe$))
      .subscribe(addresses => {
        if (addresses.length > 0) {
          for (let address of addresses) {
            if (address.primaryAddress) {
              this.selectedAddress = address;
            }
          }
          if (!this.selectedAddress) {
            this.selectedAddress = addresses[0];
          }
        }
      });
  }

  checkout() {
    if (this.selectedAddress && this.buyNowProductId) {
      this.orderService.createBuyNowOrder(this.selectedAddress, this.buyNowProductId);
    }
  }

  onAddressChange(address: Address) {
    this.selectedAddress = address;
  }

  editAddress(address: Address) {
    this.addressService.editAddress(address);
  }

  createAddress() {
    this.addressService.createAddress();
  }

  calculateTotal(cartItems: CartItem[]): number {
    if (cartItems && cartItems.length > 0) {
      return cartItems.reduce((acc, current) => acc + current.discountPrice * current.quantity, 0);
    } else {
      return 0;
    }
  }

  trackCartItem: TrackByFunction<CartItem> = (index: number, cartItem: CartItem): string => {
    return cartItem.userId + cartItem.productId;
  };

  trackAddress: TrackByFunction<Address> = (index: number, address: Address): string => {
    return address.id;
  };

}
