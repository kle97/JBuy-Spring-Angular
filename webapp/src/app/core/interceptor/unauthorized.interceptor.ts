import { Injectable } from "@angular/core";
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { catchError, Observable, throwError } from "rxjs";
import { CookieService } from "ngx-cookie";
import { AuthenticationService } from "../repository/authentication/authentication.service";
import { ErrorNotificationService } from "../service/error-notification.service";
import { NavigationExtras, Router } from "@angular/router";
import { AuthenticationRepository, initialUser } from "../repository/authentication/authentication.repository";
import { ShoppingCartRepository } from "../../shop/shopping-cart/repository/shopping-cart.repository";


@Injectable()
export class UnauthorizedInterceptor implements HttpInterceptor {

  constructor(
    private cookieService: CookieService,
    private authenticationService: AuthenticationService,
    private authenticationRepository: AuthenticationRepository,
    private shoppingCartRepository: ShoppingCartRepository,
    private errorNotificationService: ErrorNotificationService,
    private router: Router,
  ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // skip intercepting for 'login' requests
    if (request.url.includes("login")) {
      return next.handle(request);
    }

    return next.handle(request).pipe(
      catchError(errorResponse => {
        if (errorResponse instanceof HttpErrorResponse && errorResponse.status === 401) {
          return this.handle401Error(errorResponse);
        } else {
          return throwError(errorResponse);
        }
      }),
    );
  }

  private handle401Error(errorResponse: any): Observable<HttpEvent<any>> {
    this.authenticationRepository.updateUser(initialUser());
    this.authenticationRepository.updateSyncCart(false);
    this.shoppingCartRepository.setCartItems([]);

    // Set navigation extras with current url for redirecting as query params
    const navigationExtras: NavigationExtras = {
      queryParams: { redirect: this.router.url.split("?")[0].split("(")[0] },
    };

    // Redirect to the login page with extras
    this.errorNotificationService.open(new Error("Please login to continue!"), 2000);
    this.router.navigateByUrl("/login", navigationExtras);

    return throwError(errorResponse);
  }

}
