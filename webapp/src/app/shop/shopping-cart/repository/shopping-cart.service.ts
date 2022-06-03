import { Injectable } from "@angular/core";
import { AbstractGenericCrudService } from "../../../core/service/generic-crud.service";
import { HttpClient, HttpParams } from "@angular/common/http";
import { ErrorNotificationService } from "../../../core/service/error-notification.service";
import { ShoppingCartRepository } from "./shopping-cart.repository";
import { CartItem } from "../model/cart-item.model";
import { AuthenticationRepository } from "../../../core/repository/authentication/authentication.repository";
import { concat, forkJoin, last, Observable, switchMap, take, tap } from "rxjs";
import { NotificationService } from "../../../core/service/notification.service";
import { Router } from "@angular/router";

@Injectable({
  providedIn: "root",
})
export class ShoppingCartService extends AbstractGenericCrudService<CartItem, string> {

  private userId: string = "";

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
    this.authenticationRepository.user$.pipe(take(1)).subscribe(user => {
      if (user.expiry > 0 && Date.now() < user.expiry) {
        this.create({
          userId: user.id,
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
        if (user.expiry > 0 && Date.now() >= user.expiry) {
          this.shoppingCartRepository.setCartItems([]);
        }
        this.shoppingCartRepository.addCartItem(cartItem);
        if (navigate) {
          this.router.navigateByUrl("/cart");
        }
      }
    });
  }

  updateCartItem(cartItem: CartItem) {
    this.authenticationRepository.user$.pipe(take(1)).subscribe(user => {
      if (user.expiry > 0 && Date.now() < user.expiry) {
        this.update({
          userId: user.id,
          productId: cartItem.productId,
          quantity: cartItem.quantity,
        }, this.userId, cartItem.productId).subscribe(
          cartItem => this.shoppingCartRepository.updateCartItem(cartItem),
        );
      } else {
        if (user.expiry > 0 && Date.now() >= user.expiry) {
          this.shoppingCartRepository.setCartItems([]);
        } else {
          this.shoppingCartRepository.updateCartItem(cartItem);
        }
      }
    });
  }

  removeCartItem(cartItem: CartItem) {
    this.authenticationRepository.user$.pipe(take(1)).subscribe(user => {
      if (user.expiry > 0 && Date.now() < user.expiry) {
        this.delete(user.id, cartItem.productId).subscribe(
          () => this.shoppingCartRepository.removeCartItem(cartItem),
        );
      } else {
        if (user.expiry > 0 && Date.now() >= user.expiry) {
          this.shoppingCartRepository.setCartItems([]);
        } else {
          this.shoppingCartRepository.removeCartItem(cartItem);
        }
      }
    });
  }

  isLoggedIn() {
    let isLoggedIn = false;
    this.authenticationRepository.user$.pipe(
      take(1),
    ).subscribe(user => {
      this.userId = user.id;
      isLoggedIn = user.expiry > 0 && Date.now() < user.expiry;
    });
    return isLoggedIn;
  }
}
