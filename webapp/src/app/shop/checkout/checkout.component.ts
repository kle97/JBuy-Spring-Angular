import { Component, OnInit, TrackByFunction } from "@angular/core";
import { Observable, takeUntil } from "rxjs";
import { CartItem } from "../shopping-cart/model/cart-item.model";
import { ShoppingCartRepository } from "../shopping-cart/repository/shopping-cart.repository";
import { ShoppingCartService } from "../shopping-cart/repository/shopping-cart.service";
import { FormBuilder } from "@angular/forms";
import { AddressRepository } from "../account-setting/repository/address/address.repository";
import { AddressService } from "../account-setting/repository/address/address.service";
import { Address } from "../account-setting/model/address.model";
import { UnsubscribeComponent } from "../../core/component/unsubscribe/unsubscribe.component";
import { OrderService } from "../account-setting/repository/order/order.service";

@Component({
  selector: "app-checkout",
  templateUrl: "./checkout.component.html",
  styleUrls: ["./checkout.component.scss"],
})
export class CheckoutComponent extends UnsubscribeComponent implements OnInit {

  cartItemCount$: Observable<number> = this.shoppingCartRepository.cartItemCount$;
  cartItems$: Observable<Array<CartItem>> = this.shoppingCartRepository.cartItems$;
  selectedAddress!: Address;
  addresses$: Observable<Array<Address>> = this.addressRepository.addresses$;

  constructor(
    private formBuilder: FormBuilder,
    private shoppingCartRepository: ShoppingCartRepository,
    private shoppingCartService: ShoppingCartService,
    private orderService: OrderService,
    private addressRepository: AddressRepository,
    private addressService: AddressService,
  ) {
    super();
  }

  ngOnInit(): void {
    this.addressService.getPage();
    this.shoppingCartService.getCartItems().subscribe();
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
    if (this.selectedAddress) {
      this.orderService.createOrder(this.selectedAddress);
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
