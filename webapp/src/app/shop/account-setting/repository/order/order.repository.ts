import { createStore, withProps } from "@ngneat/elf";
import { Injectable } from "@angular/core";
import { Order } from "../../model/order.model";
import { Page } from "../../../../core/model/page.model";
import { emptyPage } from "../../../../core/constant/empty-page";

export const initialOrderPage: Page<Order> = emptyPage;

export const orderStore = createStore({ name: "order" }, withProps<Page<Order>>(initialOrderPage));

@Injectable({
  providedIn: "root",
})
export class OrderRepository {
  orderPage$ = orderStore.pipe(state => state);

  setOrderPage(orderPage: Page<Order>) {
    orderStore.update(() => ({ ...orderPage }));
  }
}
