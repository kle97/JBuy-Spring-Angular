import { AfterViewInit, ChangeDetectionStrategy, Component, OnInit, TrackByFunction } from "@angular/core";
import { Observable } from "rxjs";
import { ShoppingCartService } from "../repository/shopping-cart.service";
import { CartItem } from "../model/cart-item.model";
import { ShoppingCartRepository } from "../repository/shopping-cart.repository";

@Component({
  selector: "app-cart",
  templateUrl: "./cart.component.html",
  styleUrls: ["./cart.component.scss"],
})
export class CartComponent implements OnInit, AfterViewInit {

  cartItemCount$: Observable<number> = this.shoppingCartRepository.cartItemCount$;
  cartItems$: Observable<Array<CartItem>> = this.shoppingCartRepository.cartItems$;

  constructor(
    private shoppingCartRepository: ShoppingCartRepository,
    private shoppingCartService: ShoppingCartService,
  ) {
  }

  ngAfterViewInit(): void {
    this.shoppingCartService.getCartItems().subscribe();
  }

  ngOnInit(): void {
  }

  removeCartItem(cartItem: CartItem) {
    if (this.shoppingCartService.isLoggedIn()) {
      this.shoppingCartService.getCartItems().subscribe(() => {
        this.shoppingCartService.removeCartItem(cartItem);
      });
    } else {
      this.shoppingCartService.removeCartItem(cartItem);
    }

    // this.shoppingCartService.removeCartItem(cartItem);
  }

  updateCartItem(cartItem: CartItem) {
    if (this.shoppingCartService.isLoggedIn()) {
      this.shoppingCartService.getCartItems().subscribe(() => {
        this.shoppingCartService.updateCartItem(cartItem);
      });
    } else {
      this.shoppingCartService.updateCartItem(cartItem);
    }

    // this.shoppingCartService.updateCartItem(cartItem);
  }

  calculateQuantity(cartItem: CartItem): Array<number> {
    return Array.from(Array(cartItem.stock).keys()).map(i => i + 1);
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

}
