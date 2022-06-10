import { Component, Input, OnInit, TrackByFunction } from "@angular/core";
import { OrderProductService } from "../repository/order-product/order-product.service";
import { OrderProductRepository } from "../repository/order-product/order-product.repository";
import { Observable } from "rxjs";
import { OrderProduct } from "../model/order-product.model";
import { Page } from "../../../core/model/page.model";
import { PageEvent } from "@angular/material/paginator";
import { PageRequest } from "../../../core/model/page-request.model";
import { ScrollUpService } from "../../../core/service/scroll-up.service";
import { Order } from "../model/order.model";
import { ReviewService } from "../repository/review/review.service";

@Component({
  selector: "app-order-product",
  templateUrl: "./order-product.component.html",
  styleUrls: ["./order-product.component.scss"],
})
export class OrderProductComponent implements OnInit {

  orderProductPage$: Observable<Page<OrderProduct>> = this.orderProductRepository.orderProductPage$;
  @Input() order!: Order;

  constructor(
    private orderProductRepository: OrderProductRepository,
    private orderProductService: OrderProductService,
    private reviewService: ReviewService,
    private scrollUpService: ScrollUpService,
  ) {
  }

  ngOnInit(): void {
    if (this.order && this.order.id) {
      this.orderProductService.getOrderProductPage(this.order.id);
    }
  }

  writeOrEditReview(orderProduct: OrderProduct) {
    this.reviewService.createOrEditReview(orderProduct.productId);
  }

  onPageEvent(pageEvent: PageEvent) {
    const pageRequest: PageRequest = {
      page: pageEvent.pageIndex,
      size: pageEvent.pageSize,
      sort: ["name"],
    };

    if (this.order && this.order.id) {
      this.orderProductService.getOrderProductPage(this.order.id, pageRequest);
      this.scrollUpService.toTop("body");
    }
  }

  trackOrderProduct: TrackByFunction<OrderProduct> = (index: number, orderProduct: OrderProduct): string => {
    return orderProduct.orderId + orderProduct.productId;
  };

}
