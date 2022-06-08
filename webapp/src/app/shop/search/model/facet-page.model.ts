import { Page } from "../../../core/model/page.model";
import { Facet } from "./facet.model";

export interface FacetPage<T> extends Page<T>{
  facetMap: Map<string, Array<Facet>>,
}
