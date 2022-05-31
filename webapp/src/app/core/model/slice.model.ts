export interface Slice<T> {
  content: T[],
  pageable: {
    sort: {
      unsorted: boolean,
      sorted: boolean,
      empty: boolean,
    },
    pageNumber: number,
    pageSize: number,
    offset: number,
    paged: boolean,
    unpaged: boolean,
  },
  sort: {
    unsorted: boolean,
    sorted: boolean,
    empty: boolean,
  },
  first: boolean,
  last: boolean,
  size: number,
  number: number,
  numberOfElements: number,
  empty: boolean,
}
