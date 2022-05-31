import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { finalize, Observable } from "rxjs";
import { LoadingRepository } from "../repository/loading/loading.repository";

@Injectable()
export class LoadingInterceptor implements HttpInterceptor {

  constructor(
    private loadingRepository: LoadingRepository,
  ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.loadingRepository.increaseRequest();
    this.loadingRepository.loadingOn();

    return next.handle(request).pipe(
      finalize(() => {
        this.loadingRepository.decreaseRequest();
        this.loadingRepository.loadingOff();
      }),
    );
  }
}
