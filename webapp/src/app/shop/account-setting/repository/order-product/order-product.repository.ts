import { Page } from "../../../../core/model/page.model";
import { emptyPage } from "../../../../core/constant/empty-page";
import { createStore, withProps } from "@ngneat/elf";
import { Injectable } from "@angular/core";
import { OrderProduct } from "../../model/order-product.model";

export const initialOrderProductPage: Page<OrderProduct> = emptyPage;

export const orderProductStore = createStore({ name: "orderProduct" },
  withProps<Page<OrderProduct>>(initialOrderProductPage));

@Injectable({
  providedIn: "root",
})
export class OrderProductRepository {
  orderProductPage$ = orderProductStore.pipe(state => state);

  setOrderProductPage(orderProductPage: Page<OrderProduct>) {
    orderProductStore.update(() => ({ ...orderProductPage }));
  }
}
