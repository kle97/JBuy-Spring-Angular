import { NgModule } from "@angular/core";

import { ShoppingCartRoutingModule } from "./shopping-cart-routing.module";
import { CartComponent } from "./cart/cart.component";
import { NavigationBarCartComponent } from "./navigation-bar-cart/navigation-bar-cart.component";
import { SharedModule } from "../../shared/module/shared.module";
import { CoreModule } from "../../core/core.module";
import { ProductModule } from "../product/product.module";


@NgModule({
  declarations: [
    CartComponent,
    NavigationBarCartComponent,
  ],
  imports: [
    SharedModule,
    CoreModule,
    ProductModule,
    ShoppingCartRoutingModule,
  ],
  exports: [
    NavigationBarCartComponent,
  ]
})
export class ShoppingCartModule {
}
