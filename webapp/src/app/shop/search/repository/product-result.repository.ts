import { createStore, withProps } from "@ngneat/elf";
import { Product } from "../model/product.model";
import { Injectable } from "@angular/core";
import { emptyPage } from "../../../core/constant/empty-page";
import { FacetPage } from "../model/facet-page.model";

export const initialSearchResultPage: FacetPage<Product> = {
  ...emptyPage,
  facetMap: new Map(),
};

export const productResultStore = createStore({ name: "searchResult" },
  withProps<FacetPage<Product>>(initialSearchResultPage));

@Injectable({
  providedIn: "root",
})
export class ProductResultRepository {
  productResultPage$ = productResultStore.pipe(state => state);

  setProductResultPage(productResultPage: FacetPage<Product>) {
    productResultStore.update(() => ({ ...productResultPage }));
  }
}
