import { Injectable } from "@angular/core";
import { AbstractGenericCrudService } from "../../../core/service/generic-crud.service";
import { ProductDetail } from "../model/product-detail.model";
import { ErrorNotificationService } from "../../../core/service/error-notification.service";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { catchError, Observable, tap, throwError } from "rxjs";
import { ProductDetailRepository } from "./product-detail.repository";
import { ProductAttribute } from "../model/product-attribute.model";

@Injectable({
  providedIn: "root",
})
export class ProductDetailService extends AbstractGenericCrudService<ProductDetail, string> {

  constructor(
    protected override http: HttpClient,
    private errorNotificationService: ErrorNotificationService,
    private productDetailRepository: ProductDetailRepository,
  ) {
    super(http, "/products", {
      readAll: false,
      readPage: false,
      create: false,
      update: false,
      delete: false,
    });
  }

  readonly productAttributeUrl: string = "/product-attributes/:id";

  protected override handleError(errorResponse: HttpErrorResponse): Observable<never> {
    // this.errorNotificationService.open(errorResponse);
    return throwError(() => errorResponse);
  }

  getProduct(id: string): Observable<ProductDetail> {
    return this.readOne(id).pipe(tap(product => {
      this.productDetailRepository.addProduct(product);
    }))
  }

  getProductAttributes(id: string): Observable<Array<ProductAttribute>> {
    const url = this.getUrlWithId(this.productAttributeUrl, id);
    return this.http.get<Array<ProductAttribute>>(url, this.httpOptions).pipe(
      catchError(errorResponse => this.handleError(errorResponse)),
    );
  }

  slugify(text: string): string {
    return text
      .toString()
      .normalize("NFD") // split an accented letter in the base letter and the accent
      .replace(/[\u0300-\u036f]/g, "")  // remove all previously split accent
      .toLowerCase()
      .trim()
      .replace(/\s+/g, "-") // replace spaces with "-"
      .replace(/[^\w-]+/g, "") // remove non-word
      .replace(/--+/g, "-"); // replace more than one dashes with one dash
  }
}
