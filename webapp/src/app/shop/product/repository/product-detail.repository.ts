import { createStore } from "@ngneat/elf";
import { Injectable } from "@angular/core";
import { addEntitiesFifo, deleteEntities, hasEntity, selectAllEntities, withEntities } from "@ngneat/elf-entities";
import { localStorageStrategy, persistState } from "@ngneat/elf-persist-state";
import { Product } from "../../search/model/product.model";

export const productDetailStore = createStore({ name: "productDetail" }, withEntities<Product>());

@Injectable({
  providedIn: "root",
})
export class ProductDetailRepository {
  recentlyVisitedProducts$ = productDetailStore.pipe(selectAllEntities());

  addProduct(product: Product) {
    if (!productDetailStore.query(hasEntity(product.id))) {
      productDetailStore.update(addEntitiesFifo([product], { limit: 15 }));
    } else {
      productDetailStore.update(deleteEntities(product.id));
      productDetailStore.update(addEntitiesFifo([product], { limit: 15 }));
    }
  }
}

export const productPersist = persistState(productDetailStore, {
  key: "product",
  storage: localStorageStrategy,
});
