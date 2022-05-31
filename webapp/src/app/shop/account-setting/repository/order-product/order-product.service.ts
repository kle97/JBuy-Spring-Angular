import { Injectable } from '@angular/core';
import { AbstractGenericCrudService } from "../../../../core/service/generic-crud.service";
import { Order } from "../../model/order.model";
import { OrderProduct } from "../../model/order-product.model";
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { AuthenticationRepository } from "../../../../core/repository/authentication/authentication.repository";
import { OrderRepository } from "../order/order.repository";
import { ErrorNotificationService } from "../../../../core/service/error-notification.service";
import { OrderProductRepository } from "./order-product.repository";
import { Observable, take, throwError } from "rxjs";
import { PageRequest } from "../../../../core/model/page-request.model";
import { defaultPageRequest } from "../../../../core/constant/default-page-request";

@Injectable({
  providedIn: 'root'
})
export class OrderProductService extends AbstractGenericCrudService<OrderProduct, string> {

  constructor(
    protected override http: HttpClient,
    private authenticationRepository: AuthenticationRepository,
    private orderProductRepository: OrderProductRepository,
    private errorNotificationService: ErrorNotificationService,
  ) {
    super(http, "/order-products", {
      readPageUrl: "/:id/page",
      readOne: false,
      readAll: false,
      create: false,
      update: false,
      delete: false,
    });
  }

  protected override handleError(errorResponse: HttpErrorResponse): Observable<never> {
    this.errorNotificationService.open(errorResponse);
    return throwError(() => errorResponse);
  }

  getOrderProductPage(orderId: string, pageRequest: PageRequest = defaultPageRequest) {
    const httpParams: HttpParams = new HttpParams({
      fromObject: {
        page: pageRequest.page,
        size: pageRequest.size,
        sort: pageRequest.sort,
      },
    });

    this.readPage(httpParams, orderId).subscribe(orderProductPage => {
      this.orderProductRepository.setOrderProductPage(orderProductPage);
    });
  }
}
