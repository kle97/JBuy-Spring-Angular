import { NgModule } from "@angular/core";
import { SharedModule } from "../../shared/module/shared.module";
import { CoreModule } from "../../core/core.module";
import { ProductDetailComponent } from "./product-detail/product-detail.component";
import { RecentlyVisitedComponent } from "./recently-visited/recently-visited.component";
import { ProductRoutingModule } from "./product-routing.module";
import { SimilarProductComponent } from "./similar-product/similar-product.component";
import { ProductReviewComponent } from "./product-review/product-review.component";
import { NgxSkeletonLoaderModule } from "ngx-skeleton-loader";

@NgModule({
  declarations: [
    ProductDetailComponent,
    RecentlyVisitedComponent,
    SimilarProductComponent,
    ProductReviewComponent,
  ],
  imports: [
    SharedModule,
    CoreModule,
    NgxSkeletonLoaderModule,
    ProductRoutingModule,
  ],
  exports: [
    RecentlyVisitedComponent,
  ]
})
export class ProductModule {
}
