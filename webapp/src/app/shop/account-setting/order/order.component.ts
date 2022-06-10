import { Component, OnInit, TrackByFunction } from "@angular/core";
import { OrderRepository } from "../repository/order/order.repository";
import { OrderService } from "../repository/order/order.service";
import { Observable } from "rxjs";
import { Order } from "../model/order.model";
import { Page } from "../../../core/model/page.model";
import { PageRequest } from "../../../core/model/page-request.model";
import { defaultPageRequest } from "../../../core/constant/default-page-request";
import { PageEvent } from "@angular/material/paginator";
import { ScrollUpService } from "../../../core/service/scroll-up.service";

@Component({
  selector: "app-order",
  templateUrl: "./order.component.html",
  styleUrls: ["./order.component.scss"],
})
export class OrderComponent implements OnInit {

  orderDetailVisibility: boolean = false;
  order!: Order;

  orderPage$: Observable<Page<Order>> = this.orderRepository.orderPage$;

  constructor(
    private orderRepository: OrderRepository,
    private orderService: OrderService,
    private scrollUpService: ScrollUpService,
  ) {
  }

  ngOnInit(): void {
    const pageRequest: PageRequest = {
      ...defaultPageRequest,
      sort: ["orderDate,desc"],
    };

    this.orderService.getOrderPage(pageRequest);
  }

  getOrderDetails(order: Order) {
    this.orderDetailVisibility = true;
    this.order = order;
  }

  backToOrderList() {
    this.orderDetailVisibility = false;
  }

  onPageEvent(pageEvent: PageEvent) {
    const pageRequest: PageRequest = {
      page: pageEvent.pageIndex,
      size: pageEvent.pageSize,
      sort: ["orderDate,desc"],
    };

    this.orderService.getOrderPage(pageRequest);
    this.scrollUpService.toTop("body");
  }

  trackOrder: TrackByFunction<Order> = (index: number, order: Order): string => {
    return order.id;
  };

}
