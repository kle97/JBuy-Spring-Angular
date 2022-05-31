import { createStore } from "@ngneat/elf";
import {
  addEntities,
  deleteEntities,
  getEntitiesCount,
  hasEntity,
  selectAllEntities,
  setEntities,
  updateEntities,
  withEntities,
} from "@ngneat/elf-entities";
import { Injectable } from "@angular/core";
import { localStorageStrategy, persistState } from "@ngneat/elf-persist-state";
import { CartItem } from "../model/cart-item.model";
import { map, Observable, of, switchMap } from "rxjs";

export const shoppingCartStore = createStore(
  { name: "shoppingCart" },
  withEntities<CartItem, "productId">({ idKey: "productId" }),
);

@Injectable({
  providedIn: "root",
})
export class ShoppingCartRepository {
  cartItems$: Observable<CartItem[]> = shoppingCartStore.pipe(
    selectAllEntities(),
    map(cartItems => {
      return cartItems.sort((a, b) => a.name.localeCompare(b.name));
    })
  );
  cartItemCount$: Observable<number> = this.cartItems$.pipe(
    switchMap(cartItems => {
      const total = cartItems.reduce((acc, current) => {
        return acc + current.quantity;
      }, 0);
      return of(total);
    }),
  );

  setCartItems(cartItems: CartItem[]) {
    shoppingCartStore.update(setEntities(cartItems));
  }

  addCartItem(cartItem: CartItem) {
    if (!shoppingCartStore.query(hasEntity(cartItem.productId))) {
      if (shoppingCartStore.query(getEntitiesCount()) <= 20) {
        shoppingCartStore.update(addEntities([cartItem]));
      }
    } else {
      this.addQuantityToCartItem(cartItem);
    }
  }

  updateCartItem(cartItem: CartItem) {
    if (shoppingCartStore.query(hasEntity(cartItem.productId))) {
      shoppingCartStore.update(updateEntities(cartItem.productId, (entity) => ({
        ...entity,
        quantity: cartItem.quantity,
      })));
    }
  }

  addQuantityToCartItem(cartItem: CartItem) {
    if (shoppingCartStore.query(hasEntity(cartItem.productId))) {
      shoppingCartStore.update(updateEntities(cartItem.productId, (entity) => ({
        ...entity,
        quantity: cartItem.quantity + entity.quantity,
      })));
    }
  }

  removeCartItem(cartItem: CartItem) {
    shoppingCartStore.update(deleteEntities(cartItem.productId));
  }
}

export const shoppingCartPersist = persistState(shoppingCartStore, {
  key: "shopping-cart",
  storage: localStorageStrategy,
});
