import { Component, OnInit, TrackByFunction } from "@angular/core";
import { BehaviorSubject, Observable, takeUntil } from "rxjs";
import { MatMenuTrigger } from "@angular/material/menu";
import { ShoppingCartRepository } from "../repository/shopping-cart.repository";
import { ShoppingCartService } from "../repository/shopping-cart.service";
import { CartItem } from "../model/cart-item.model";
import { NavigationEnd, Router } from "@angular/router";
import { UnsubscribeComponent } from "../../../core/component/unsubscribe/unsubscribe.component";

@Component({
  selector: "app-navigation-bar-cart",
  templateUrl: "./navigation-bar-cart.component.html",
  styleUrls: ["./navigation-bar-cart.component.scss"],
})
export class NavigationBarCartComponent extends UnsubscribeComponent implements OnInit {

  timedOutCloser!: NodeJS.Timeout;
  cartItemCount$: Observable<number> = this.shoppingCartRepository.cartItemCount$;
  cartItems$: Observable<Array<CartItem>> = this.shoppingCartRepository.cartItems$;
  url$ = new BehaviorSubject<string>("");

  constructor(
    private shoppingCartRepository: ShoppingCartRepository,
    private shoppingCartService: ShoppingCartService,
    private router: Router,
  ) {
    super();
  }

  ngOnInit(): void {
    this.url$.next(this.router.url);
    this.router.events.pipe(takeUntil(this.unsubscribe$)).subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.url$.next(event.url);
      }
    });
  }

  removeCartItem(cartItem: CartItem) {
    this.shoppingCartService.removeCartItem(cartItem);
  }

  getTotal(cartItems: CartItem[]): number {
    return cartItems.reduce((prevTotal, current) => prevTotal + current.discountPrice * current.quantity, 0);
  }

  mouseEnter(trigger: MatMenuTrigger) {
    if (this.timedOutCloser) {
      clearTimeout(this.timedOutCloser);
    }
    setTimeout(() => {
      trigger.openMenu();
    });
  }

  mouseLeave(trigger: MatMenuTrigger) {
    this.timedOutCloser = setTimeout(() => {
      trigger.closeMenu();
    }, 50);
  }

  trackCartItem: TrackByFunction<CartItem> = (index: number, cartItem: CartItem): string => {
    return cartItem.userId + cartItem.productId;
  };

}
