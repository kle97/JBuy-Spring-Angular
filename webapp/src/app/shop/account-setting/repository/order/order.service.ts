import { Injectable } from "@angular/core";
import { AbstractGenericCrudService } from "../../../../core/service/generic-crud.service";
import { Order } from "../../model/order.model";
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { AuthenticationRepository } from "../../../../core/repository/authentication/authentication.repository";
import { ErrorNotificationService } from "../../../../core/service/error-notification.service";
import { PageRequest } from "../../../../core/model/page-request.model";
import { defaultPageRequest } from "../../../../core/constant/default-page-request";
import { catchError, Observable, of, switchMap, take, throwError } from "rxjs";
import { OrderRepository } from "./order.repository";
import { Address } from "../../model/address.model";

@Injectable({
  providedIn: "root",
})
export class OrderService extends AbstractGenericCrudService<Order, string> {

  constructor(
    protected override http: HttpClient,
    private authenticationRepository: AuthenticationRepository,
    private orderRepository: OrderRepository,
    private errorNotificationService: ErrorNotificationService,
  ) {
    super(http, "/orders", {
      readPageUrl: "/:id/page",
      readAll: false,
      update: false,
      delete: false,
    });
  }

  private readonly createBuyNowUrl = "/buy-now";

  protected override handleError(errorResponse: HttpErrorResponse): Observable<never> {
    this.errorNotificationService.open(errorResponse);
    return throwError(() => errorResponse);
  }

  getOrderPage(pageRequest: PageRequest = defaultPageRequest) {
    this.authenticationRepository.user$.pipe(take(1))
      .subscribe(user => {
        if (user.id) {
          const httpParams: HttpParams = new HttpParams({
            fromObject: {
              page: pageRequest.page,
              size: pageRequest.size,
              sort: pageRequest.sort,
            },
          });

          this.readPage(httpParams, user.id).subscribe(orderPage => {
            this.orderRepository.setOrderPage(orderPage);
          });
        }
      });
  }

  createBuyNowOrder(address: Address, productId: string): Observable<Order> {
    return this.authenticationRepository.user$.pipe(
      take(1),
      switchMap(user => {
        let userId = "00000000-0000-0000-0000-000000000000";
        if (user.id) {
          userId = user.id;
        }

        const url = this.entityUrl + this.createBuyNowUrl;

        return this.http.post<Order>(url, {
          userId: userId,
          productId: productId,
          shippingAddressLine1: address.addressLine1,
          shippingAddressLine2: address.addressLine2,
          shippingCity: address.city,
          shippingState: address.state,
          shippingPostalCode: address.postalCode,
        }, this.httpOptions).pipe(catchError(errorResponse => this.handleError(errorResponse)));
      }),
    );
  }

  createOrder(address: Address): Observable<Order> {
    return this.authenticationRepository.user$.pipe(
      take(1),
      switchMap(user => {
        let userId = "00000000-0000-0000-0000-000000000000";
        if (user.id) {
          userId = user.id;
        }

        return this.create({
          userId: userId,
          shippingAddressLine1: address.addressLine1,
          shippingAddressLine2: address.addressLine2,
          shippingCity: address.city,
          shippingState: address.state,
          shippingPostalCode: address.postalCode,
        });
      }),
    );
  }

  getOrder(orderId: string): Observable<Order> {
    return this.readOne(orderId);
  }
}
