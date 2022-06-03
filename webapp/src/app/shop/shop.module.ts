import { NgModule } from "@angular/core";

import { ShopRoutingModule } from "./shop-routing.module";
import { CoreModule } from "../core/core.module";
import { ShopNavigationBarComponent } from "./shop-navigation-bar/shop-navigation-bar.component";
import { SharedModule } from "../shared/module/shared.module";
import { ProductModule } from "./product/product.module";
import { ShopComponent } from "./shop.component";
import { ShoppingCartModule } from "./shopping-cart/shopping-cart.module";
import { SearchModule } from "./search/search.module";
import { CheckoutModule } from "./checkout/checkout.module";
import { AccountSettingModule } from "./account-setting/account-setting.module";
import { HomeModule } from "./home/home.module";


@NgModule({
  declarations: [
    ShopNavigationBarComponent,
    ShopComponent,
  ],
  imports: [
    SharedModule,
    CoreModule,
    HomeModule,
    ProductModule,
    ShoppingCartModule,
    SearchModule,
    CheckoutModule,
    AccountSettingModule,
    ShopRoutingModule,
  ],
  exports: [
    ShopNavigationBarComponent,
  ],
})
export class ShopModule {
}
