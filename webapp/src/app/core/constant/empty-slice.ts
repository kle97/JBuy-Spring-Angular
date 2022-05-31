import { Slice } from "../model/slice.model";

export const emptySlice: Slice<any> = {
  content: [],
  pageable: {
    sort: {
      unsorted: false,
      sorted: true,
      empty: false,
    },
    pageNumber: 0,
    pageSize: 20,
    offset: 0,
    paged: true,
    unpaged: false,
  },
  sort: {
    unsorted: false,
    sorted: true,
    empty: false,
  },
  first: true,
  last: true,
  size: 20,
  number: 0,
  numberOfElements: 0,
  empty: true,
}
