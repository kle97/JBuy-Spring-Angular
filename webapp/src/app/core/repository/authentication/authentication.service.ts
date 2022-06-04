import { Injectable } from "@angular/core";
import { environment } from "../../../../environments/environment";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { AuthenticationRepository, initialUser } from "./authentication.repository";
import { CookieService } from "ngx-cookie";
import { Router } from "@angular/router";
import { ErrorNotificationService } from "../../service/error-notification.service";
import { BehaviorSubject, catchError, Observable, tap, throwError } from "rxjs";
import { User } from "../../model/user.model";
import { ShoppingCartService } from "../../../shop/shopping-cart/repository/shopping-cart.service";
import { ShoppingCartRepository } from "../../../shop/shopping-cart/repository/shopping-cart.repository";

@Injectable({
  providedIn: "root",
})
export class AuthenticationService {

  readonly authApiPath = "/auth";
  readonly csrfUrl = this.authApiPath + "/csrf";
  readonly loginUrl = this.authApiPath + "/login";
  readonly logoutUrl = this.authApiPath + "/logout";
  errorMessage$ = new BehaviorSubject("");

  readonly homepage = environment.homepage;
  readonly loginPage = "/login";

  httpOptions = {
    headers: new HttpHeaders({ "Content-Type": "application/json" }),
    withCredentials: true,
  };

  constructor(
    private http: HttpClient,
    private authenticationRepository: AuthenticationRepository,
    private shoppingCartRepository: ShoppingCartRepository,
    private shoppingCartService: ShoppingCartService,
    private cookieService: CookieService,
    private router: Router,
    private errorNotificationService: ErrorNotificationService,
  ) {
  }

  handleLoginError(errorResponse: any): Observable<never> {
    this.errorMessage$.next(errorResponse.error.message);
    return throwError(errorResponse);
  }

  handleLogoutError(errorResponse: any): Observable<never> {
    this.errorNotificationService.open(errorResponse);
    return throwError(errorResponse);
  }

  getCsrfToken(): Observable<any> {
    return this.http.get(this.csrfUrl, this.httpOptions);
  }

  login(email: string, password: string, redirectUrl: string = "") {
    this.errorMessage$.next("");
    const base64Str = btoa(email + ":" + password);
    const httpOptions = {
      ...this.httpOptions,
      headers: this.httpOptions.headers.append("Authorization", "Basic " + base64Str),
    };

    this.http.post<User>(this.loginUrl, {}, httpOptions).pipe(
      tap(authenticationUser => {
        this.authenticationRepository.updateUser(authenticationUser);
        this.authenticationRepository.updateSyncCart(false);
        this.shoppingCartService.getCartItems().subscribe();
        if (redirectUrl) {
          this.router.navigateByUrl(redirectUrl);
        } else {
          this.router.navigateByUrl(this.homepage);
        }
      }),
      catchError(errorResponse => this.handleLoginError(errorResponse)),
    ).subscribe();
  }

  logout(navigateToUrl: string = this.homepage) {
    this.http.post(this.logoutUrl, {}, this.httpOptions).pipe(
      tap(() => {
        this.reset();
        this.shoppingCartRepository.setCartItems([]);
        if (navigateToUrl === this.loginPage) {
          this.router.navigateByUrl(navigateToUrl);
        } else {
          this.router.navigateByUrl(navigateToUrl).then(() => {
            location.reload();
          });
        }
      }),
      catchError(errorResponse => this.handleLogoutError(errorResponse)),
    ).subscribe();
  }

  reset() {
    this.authenticationRepository.updateUser(initialUser());
    this.authenticationRepository.updateSyncCart(false);
  }
}
