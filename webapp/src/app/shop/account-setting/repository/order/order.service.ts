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
import { Router } from "@angular/router";
import { ShoppingCartService } from "../../../shopping-cart/repository/shopping-cart.service";

@Injectable({
  providedIn: "root",
})
export class OrderService extends AbstractGenericCrudService<Order, string> {

  constructor(
    protected override http: HttpClient,
    private authenticationRepository: AuthenticationRepository,
    private orderRepository: OrderRepository,
    private shoppingCartService: ShoppingCartService,
    private router: Router,
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
        if (user.expiry > 0 && Date.now() < user.expiry) {
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

  createBuyNowOrder(address: Address, productId: string) {
    this.authenticationRepository.user$.pipe(
      take(1),
    ).subscribe(user => {
      if (user.expiry > 0 && Date.now() < user.expiry) {
          const url = this.entityUrl + this.createBuyNowUrl;
          this.http.post<Order>(url, {
            userId: user.id,
            productId: productId,
            shippingAddressLine1: address.addressLine1,
            shippingAddressLine2: address.addressLine2,
            shippingCity: address.city,
            shippingState: address.state,
            shippingPostalCode: address.postalCode,
          }, this.httpOptions)
            .pipe(catchError(errorResponse => this.handleError(errorResponse)))
            .subscribe(order => {
              this.shoppingCartService.getCartItems().subscribe();
              this.router.navigate(["checkout-complete", order.id]);
            });
      }
    });
  }

  createOrder(address: Address) {
    return this.authenticationRepository.user$.pipe(
      take(1),
    ).subscribe(user => {
      if (user.expiry > 0 && Date.now() < user.expiry) {
          this.create({
            userId: user.id,
            shippingAddressLine1: address.addressLine1,
            shippingAddressLine2: address.addressLine2,
            shippingCity: address.city,
            shippingState: address.state,
            shippingPostalCode: address.postalCode,
          }).subscribe(order => {
            this.shoppingCartService.getCartItems().subscribe();
            this.router.navigate(["checkout-complete", order.id]);
          });
      }
    });
  }

  getOrder(orderId: string): Observable<Order> {
    return this.readOne(orderId);
  }
}
