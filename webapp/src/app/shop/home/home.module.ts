import { NgModule } from "@angular/core";

import { HomeRoutingModule } from "./home-routing.module";
import { CoreModule } from "../../core/core.module";
import { SharedModule } from "../../shared/module/shared.module";
import { HomeComponent } from "./home.component";
import { HomeCarouselComponent } from './home-carousel/home-carousel.component';
import { PopularCategoriesComponent } from './popular-categories/popular-categories.component';
import { ProductModule } from "../product/product.module";


@NgModule({
  declarations: [
    HomeComponent,
    HomeCarouselComponent,
    PopularCategoriesComponent,
  ],
  imports: [
    SharedModule,
    CoreModule,
    ProductModule,
    HomeRoutingModule,
  ],
  exports: [
    HomeComponent,
  ],
})
export class HomeModule {
}
