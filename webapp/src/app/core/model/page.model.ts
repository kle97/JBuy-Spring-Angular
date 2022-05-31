import { Slice } from "./slice.model";

export interface Page<T> extends Slice<T>{
  totalPages: number,
  totalElements: number,
}
