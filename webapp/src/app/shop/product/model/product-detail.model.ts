import { Product } from "../../search/model/product.model";
import { ProductAttribute } from "./product-attribute.model";
import { Category } from "./category.model";

export interface ProductDetail extends Product {
  description: string,
  thumbnails: string,
  listOfCategory: Array<Category>,
}
