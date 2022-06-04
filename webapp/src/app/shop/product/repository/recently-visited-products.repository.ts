import { createStore } from "@ngneat/elf";
import { Injectable } from "@angular/core";
import { addEntitiesFifo, deleteEntities, hasEntity, selectAllEntities, withEntities } from "@ngneat/elf-entities";
import { localStorageStrategy, persistState } from "@ngneat/elf-persist-state";
import { Product } from "../../search/model/product.model";

export const recentlyVisitedProductsStore = createStore({ name: "recentlyVisitedProducts" }, withEntities<Product>());

@Injectable({
  providedIn: "root",
})
export class RecentlyVisitedProductsRepository {
  recentlyVisitedProducts$ = recentlyVisitedProductsStore.pipe(selectAllEntities());

  addProduct(product: Product) {
    if (!recentlyVisitedProductsStore.query(hasEntity(product.id))) {
      recentlyVisitedProductsStore.update(addEntitiesFifo([product], { limit: 15 }));
    } else {
      recentlyVisitedProductsStore.update(deleteEntities(product.id));
      recentlyVisitedProductsStore.update(addEntitiesFifo([product], { limit: 15 }));
    }
  }
}

export const recentlyVisitedProductsPersist = persistState(recentlyVisitedProductsStore, {
  key: "recentlyVisitedProducts",
  storage: localStorageStrategy,
});
