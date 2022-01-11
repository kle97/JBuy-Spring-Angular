import { NgModule } from "@angular/core";

import { ShopRoutingModule } from "./shop-routing.module";
import { SharedModule } from "../shared/module/shared.module";


@NgModule({
  declarations: [],
  imports: [
    SharedModule,
    ShopRoutingModule,
  ],
})
export class ShopModule {
}
