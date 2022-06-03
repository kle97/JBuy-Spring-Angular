import { Injectable } from "@angular/core";
import { AbstractGenericCrudService } from "../../../core/service/generic-crud.service";
import { Category } from "../../product/model/category.model";
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { PageRequest } from "../../../core/model/page-request.model";
import { defaultPageRequest } from "../../../core/constant/default-page-request";
import { CategoryRepository } from "./category.repository";

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends AbstractGenericCrudService<Category, string> {

  constructor(
    protected override http: HttpClient,
    private categoryRepository: CategoryRepository,
  ) {
    super(http, "/categories", {
      readPageUrl: "",
      readOne: false,
      readAll: false,
      create: false,
      update: false,
      delete: false,
    });
  }

  protected override handleError(errorResponse: HttpErrorResponse): Observable<never> {
    return throwError(() => errorResponse);
  }

  getCategoryPage(pageRequest: PageRequest = defaultPageRequest) {
    const httpParams: HttpParams = new HttpParams({
      fromObject: {
        page: pageRequest.page,
        size: pageRequest.size,
        sort: pageRequest.sort,
      },
    });

    this.readPage(httpParams).subscribe(categoryPage => {
      this.categoryRepository.setCategoryPage(categoryPage);
    });
  }
}
