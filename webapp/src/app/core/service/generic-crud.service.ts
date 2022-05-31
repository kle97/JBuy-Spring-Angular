import { GenericCrudService } from "./generic-crud.service.interface";
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { catchError, Observable, throwError } from "rxjs";
import { Page } from "../model/page.model";

interface Options {
  readOneUrl?: string,
  readAllUrl?: string,
  readPageUrl?: string,
  createUrl?: string,
  updateUrl?: string,
  deleteUrl?: string,
  readOne?: boolean,
  readAll?: boolean,
  readPage?: boolean,
  create?: boolean,
  update?: boolean,
  delete?: boolean,
}

export interface Pageable {
  page?: number,
  size?: number,
  sort?: string[],
}

export abstract class AbstractGenericCrudService<T, ID> implements GenericCrudService<T, ID> {

  readonly defaultPageable: Pageable = {
    page: 0,
    size: 20,
    sort: [],
  };

  pageable: Pageable = this.defaultPageable;

  readonly defaultOptions: Options = {
    readOneUrl: "/:id",
    readAllUrl: "",
    readPageUrl: "/page",
    createUrl: "",
    updateUrl: "/:id",
    deleteUrl: "/:id",
    readOne: true,
    readAll: true,
    readPage: true,
    create: true,
    update: true,
    delete: true,
  };

  readonly options: Options;

  protected httpOptions = {
    headers: new HttpHeaders({ "Content-Type": "application/json" }),
    withCredentials: true,
  };

  protected constructor(
    protected http: HttpClient,
    protected readonly entityUrl: string,
    options?: Options,
  ) {
    this.options = { ...this.defaultOptions, ...options };
  }

  // override this method to apply custom error handler
  protected handleError(errorResponse: HttpErrorResponse): Observable<never> {
    return throwError(() => errorResponse);
  }

  protected getUrlWithId(url: string, id1?: ID, id2?: ID, id3?: ID): string {
    return url
      .replace("\/:id", `/${id1}`)
      .replace("\/:id", `/${id2}`)
      .replace("\/:id", `/${id3}`);
  }

  readOne(id1: ID, id2?: ID, id3?: ID): Observable<T> {
    if (!this.options.readOne) {
      throw new Error("Unsupported Operation!");
    }
    const url = this.getUrlWithId(this.entityUrl + this.options.readOneUrl, id1, id2, id3);
    return this.http.get<T>(url, this.httpOptions).pipe(
      catchError(errorResponse => this.handleError(errorResponse)),
    );
  }

  readAll(queryParams?: HttpParams, id?: ID): Observable<Array<T>> {
    if (!this.options.readAll) {
      throw new Error("Unsupported Operation!");
    }
    const url = this.getUrlWithId(this.entityUrl + this.options.readAllUrl, id);
    return this.http
      .get<Array<T>>(url, { ...this.httpOptions, params: queryParams })
      .pipe(catchError(errorResponse => this.handleError(errorResponse)));
  }

  readPage(queryParams?: HttpParams, id?: ID): Observable<Page<T>> {
    if (!this.options.readPage) {
      throw new Error("Unsupported Operation!");
    }
    const url = this.getUrlWithId(this.entityUrl + this.options.readPageUrl, id);
    return this.http
      .get<Page<T>>(url, { ...this.httpOptions, params: queryParams })
      .pipe(catchError(errorResponse => this.handleError(errorResponse)));
  }

  create(payload: Partial<T>, id?: ID): Observable<T> {
    if (!this.options.create) {
      throw new Error("Unsupported Operation!");
    }
    const url = this.getUrlWithId(this.entityUrl + this.options.createUrl, id);
    return this.http
      .post<T>(url, payload, this.httpOptions)
      .pipe(catchError(errorResponse => this.handleError(errorResponse)));
  }

  update(payload: Partial<T>, id1: ID, id2?: ID, id3?: ID): Observable<T> {
    if (!this.options.update) {
      throw new Error("Unsupported Operation!");
    }
    const url = this.getUrlWithId(this.entityUrl + this.options.updateUrl, id1, id2, id3);
    return this.http
      .put<T>(url, payload, this.httpOptions)
      .pipe(catchError(errorResponse => this.handleError(errorResponse)));
  }

  delete(id1: ID, id2?: ID, id3?: ID): Observable<any> {
    if (!this.options.delete) {
      throw new Error("Unsupported Operation!");
    }
    const url = this.getUrlWithId(this.entityUrl + this.options.deleteUrl, id1, id2, id3);
    return this.http
      .delete(url, this.httpOptions)
      .pipe(catchError(errorResponse => this.handleError(errorResponse)));
  }
}
