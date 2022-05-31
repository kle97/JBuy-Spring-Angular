import { Page } from "../model/page.model";
import { emptySlice } from "./empty-slice";

export const emptyPage: Page<any> = {
  ...emptySlice,
  totalPages: 0,
  totalElements: 0,
}
