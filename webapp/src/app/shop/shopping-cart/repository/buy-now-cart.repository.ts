import { createStore } from "@ngneat/elf";
import { selectAllEntities, setEntities, withEntities } from "@ngneat/elf-entities";
import { CartItem } from "../model/cart-item.model";
import { Injectable } from "@angular/core";
import { Observable, of, switchMap } from "rxjs";
import { localStorageStrategy, persistState } from "@ngneat/elf-persist-state";

export const buyNowCartStore = createStore(
  { name: "buyNowCart" },
  withEntities<CartItem, "productId">({ idKey: "productId" }),
);

@Injectable({
  providedIn: "root",
})
export class BuyNowCartRepository {
  cartItems$: Observable<CartItem[]> = buyNowCartStore.pipe(selectAllEntities());
  cartItemCount$: Observable<number> = this.cartItems$.pipe(
    switchMap(cartItems => {
      return of(cartItems.length > 0 ? cartItems[0].quantity : 0);
    }),
  );

  setBuyNowItem(cartItem: CartItem) {
    buyNowCartStore.update(setEntities([cartItem]));
  }
}

export const buyNowPersist = persistState(buyNowCartStore, {
  key: "buy-now-cart",
  storage: localStorageStrategy,
});
