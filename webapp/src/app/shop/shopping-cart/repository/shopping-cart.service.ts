import { Injectable } from "@angular/core";
import { AbstractGenericCrudService } from "../../../core/service/generic-crud.service";
import { HttpClient, HttpParams } from "@angular/common/http";
import { ErrorNotificationService } from "../../../core/service/error-notification.service";
import { ShoppingCartRepository } from "./shopping-cart.repository";
import { CartItem } from "../model/cart-item.model";
import { AuthenticationRepository } from "../../../core/repository/authentication/authentication.repository";
import { catchError, concat, forkJoin, last, Observable, switchMap, take, tap } from "rxjs";
import { NotificationService } from "../../../core/service/notification.service";
import { Router } from "@angular/router";

@Injectable({
  providedIn: "root",
})
export class ShoppingCartService extends AbstractGenericCrudService<CartItem, string> {

  private userId: string = "";
  private cartItemCountUrl: string = "/:id/count";

  constructor(
    protected override http: HttpClient,
    private authenticationRepository: AuthenticationRepository,
    private notificationService: NotificationService,
    private errorNotificationService: ErrorNotificationService,
    private shoppingCartRepository: ShoppingCartRepository,
    private router: Router,
  ) {
    super(http, "/cart-items", {
      readAllUrl: "/:id",
      updateUrl: "/:id/products/:id",
      deleteUrl: "/:id/products/:id",
      readOne: false,
    });
  }

  getCartItemCount(): Observable<number> {
    if (this.isLoggedIn()) {
      const url = this.getUrlWithId(this.entityUrl + this.cartItemCountUrl, this.userId);
      return this.http.get<number>(url, this.httpOptions).pipe(
        catchError(errorResponse => this.handleError(errorResponse)),
      );
    } else {
      return this.shoppingCartRepository.cartItemCount$;
    }
  }

  getCartItems(): Observable<CartItem[]> {
    if (this.isLoggedIn()) {
      return forkJoin([
        this.shoppingCartRepository.cartItems$.pipe(take(1)),
        this.authenticationRepository.syncCart$.pipe(take(1)),
      ]).pipe(
        switchMap(([cartItems, syncCart]) => {
          if (syncCart) {
            return this.readAll(new HttpParams(), this.userId).pipe(
              tap(cartItems => this.shoppingCartRepository.setCartItems(cartItems)),
            );
          } else {
            this.authenticationRepository.updateSyncCart(true);
            if (cartItems.length === 0) {
              return this.readAll(new HttpParams(), this.userId).pipe(
                tap(cartItems => this.shoppingCartRepository.setCartItems(cartItems)),
              );
            } else {
              return concat(...cartItems.map(cartItem => this.create({
                userId: this.userId,
                productId: cartItem.productId,
                quantity: cartItem.quantity,
              }, this.userId))).pipe(
                last(),
                switchMap(() => {
                  return this.readAll(new HttpParams(), this.userId).pipe(
                    tap(cartItems => this.shoppingCartRepository.setCartItems(cartItems)),
                  );
                }),
              );
            }
          }
        }),
      );
    } else {
      return this.shoppingCartRepository.cartItems$;
    }
  }

  addCartItem(cartItem: CartItem, navigate: boolean = true) {
    if (this.isLoggedIn()) {
      this.create({
        userId: this.userId,
        productId: cartItem.productId,
        quantity: cartItem.quantity,
      }, this.userId).subscribe(
        () => {
          this.shoppingCartRepository.addCartItem(cartItem);
          if (navigate) {
            this.router.navigateByUrl("/cart");
          }
        },
      );
    } else {
      this.shoppingCartRepository.addCartItem(cartItem);
      if (navigate) {
        this.router.navigateByUrl("/cart");
      }
    }
  }

  updateCartItem(cartItem: CartItem) {
    if (this.isLoggedIn()) {
      this.update({
        userId: this.userId,
        productId: cartItem.productId,
        quantity: cartItem.quantity,
      }, this.userId, cartItem.productId).subscribe(
        cartItem => this.shoppingCartRepository.updateCartItem(cartItem),
      );
    } else {
      this.shoppingCartRepository.updateCartItem(cartItem);
    }
  }

  removeCartItem(cartItem: CartItem) {
    if (this.isLoggedIn()) {
      this.delete(this.userId, cartItem.productId).subscribe(
        () => this.shoppingCartRepository.removeCartItem(cartItem),
      );
    } else {
      this.shoppingCartRepository.removeCartItem(cartItem);
    }
  }

  isLoggedIn() {
    this.authenticationRepository.user$.pipe(
      take(1),
    ).subscribe(user => this.userId = user.id);
    return !!this.userId;
  }
}
