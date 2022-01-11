import { Injectable } from "@angular/core";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../environments/environment";

@Injectable()
export class ApiUrlInterceptor implements HttpInterceptor {

  readonly apiUrl = environment.apiUrl;

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    request = request.clone({ url: `${this.apiUrl}${request.url}` });
    // console.log(request.url);
    // console.log(request.params.toString());
    return next.handle(request);
  }

}
