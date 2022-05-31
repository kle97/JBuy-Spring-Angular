import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from "@angular/common/http";
import { BehaviorSubject, catchError, filter, finalize, Observable, switchMap, throwError } from "rxjs";
import { CookieService } from "ngx-cookie";
import { AuthenticationService } from "../repository/authentication/authentication.service";

@Injectable()
export class CsrfInterceptor implements HttpInterceptor {

  private isCsrfProcessing = false;
  private isCsrfProcessedSubject = new BehaviorSubject<boolean>(false);

  constructor(
    private cookieService: CookieService,
    private authenticationService: AuthenticationService,
  ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (request.method !== "GET") {
      let xsrfToken = this.cookieService.get("XSRF-TOKEN");

      if (xsrfToken == null || xsrfToken.length === 0 || xsrfToken === "") {
        return this.handleMissingCsrfToken(request, next);
      }
    }

    return next.handle(request);
  }

  private handleMissingCsrfToken(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isCsrfProcessing) {
      this.isCsrfProcessing = true;
      this.isCsrfProcessedSubject.next(false);

      return this.authenticationService.getCsrfToken().pipe(
        switchMap(() => {
          const headerName = "X-XSRF-TOKEN";
          const cookieName = "XSRF-TOKEN";
          let cookieToken = this.cookieService.get(cookieName);
          if (!cookieToken) {
            cookieToken = "";
          }
          request = request.clone({ headers: request.headers.set(headerName, cookieToken) });

          this.isCsrfProcessedSubject.next(true);
          return next.handle(request);
        }),
        catchError(errorResponse => {
          return throwError(errorResponse);
        }),
        finalize(() => {
          this.isCsrfProcessing = false;
        }),
      );
    } else {
      return this.isCsrfProcessedSubject.pipe(
        filter(isRefreshed => isRefreshed),
        switchMap(() => {
          return next.handle(request);
        }),
      );
    }
  }
}
