import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Router } from "@angular/router";
import { BehaviorSubject, catchError, Observable, tap, throwError } from "rxjs";
import { RequestUser } from "../model/request-user.model";
import { User } from "../model/user.model";
import { NotificationService } from "./notification.service";
import { ErrorNotificationService } from "./error-notification.service";
import { AuthenticationService } from "../repository/authentication/authentication.service";

@Injectable({
  providedIn: "root",
})
export class SignUpService {

  readonly signUpUrl = "/customers";
  readonly emailCheckUrl = "/users/email-check";
  readonly loginPage = "/login";
  errorMessage$ = new BehaviorSubject("");

  httpOptions = {
    headers: new HttpHeaders({ "Content-Type": "application/json" }),
    withCredentials: true,
  };

  constructor(
    private http: HttpClient,
    private router: Router,
    private authenticationService: AuthenticationService,
    private notificationService: NotificationService,
    private errorNotificationService: ErrorNotificationService,
  ) {
  }

  handleError(errorResponse: any): Observable<never> {
    this.errorNotificationService.open(errorResponse);
    return throwError(errorResponse);
  }

  handleSignUpError(errorResponse: any): Observable<never> {
    this.errorMessage$.next(errorResponse.error.message);
    return throwError(errorResponse);
  }

  isEmailAlreadyRegistered(email: string): Observable<boolean> {
    return this.http.post<boolean>(this.emailCheckUrl, { email }, this.httpOptions).pipe(
      catchError(errorResponse => this.handleError(errorResponse)),
    );
  }

  signUp(email: string, password: string, firstName: string, lastName: string) {
    const requestBody: RequestUser = {
      email,
      password,
      firstName,
      lastName,
    };
    this.errorMessage$.next("");
    this.http.post<User>(this.signUpUrl, requestBody, this.httpOptions).pipe(
      tap(authenticationUser => {
        this.authenticationService.login(email, password);
        this.notificationService.open("You have successfully created an account with email "
          + authenticationUser.email + "!");
        // this.router.navigate([this.loginPage]);
        // this.notificationService.open("You have successfully created an account with email "
        //   + authenticationUser.email + "! Please login with your new account.");
      }),
      catchError(errorResponse => this.handleSignUpError(errorResponse)),
    ).subscribe();
  }
}
