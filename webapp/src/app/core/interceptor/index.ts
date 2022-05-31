/** Http interceptor providers in outside-in order */
import { HTTP_INTERCEPTORS } from "@angular/common/http";
import { ApiUrlInterceptor } from "./api-url.interceptor";
import { LoadingInterceptor } from "./loading.interceptor";
import { CsrfInterceptor } from "./csrf.interceptor";
import { UnauthorizedInterceptor } from "./unauthorized.interceptor";

export const httpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: ApiUrlInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: CsrfInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: UnauthorizedInterceptor, multi: true },
];
