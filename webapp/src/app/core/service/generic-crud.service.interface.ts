import { Observable } from "rxjs";
import { HttpParams } from "@angular/common/http";
import { Page } from "../model/page.model";

export interface GenericCrudService<T, ID> {
  create(payload: Partial<T>, id?: ID): Observable<T>;
  update(payload: Partial<T>, id1: ID, id2?: ID, id3?: ID): Observable<T>;
  readOne(id1: ID, id2?: ID, id3?: ID): Observable<T>;
  readAll(queryParams?: HttpParams, id?: ID): Observable<Array<T>>;
  readPage(queryParams?: HttpParams, id?: ID): Observable<Page<T>>;
  delete(id1: ID, id2?: ID, id3?: ID): Observable<any>;
}
