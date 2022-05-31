import { createStore } from "@ngneat/elf";
import { addEntitiesFifo, deleteEntities, hasEntity, selectAllEntities, withEntities } from "@ngneat/elf-entities";
import { Product } from "../model/product.model";
import { Injectable } from "@angular/core";

export const productStore = createStore({ name: "product" }, withEntities<Product>());

@Injectable({
  providedIn: "root",
})
export class ProductRepository {
  recentlyVisitedProducts$ = productStore.pipe(selectAllEntities());

  addProduct(product: Product) {
    if (!productStore.query(hasEntity(product.id))) {
      productStore.update(addEntitiesFifo([product], { limit: 15 }));
    } else {
      productStore.update(deleteEntities(product.id));
      productStore.update(addEntitiesFifo([product], { limit: 15 }));
    }
  }
}
