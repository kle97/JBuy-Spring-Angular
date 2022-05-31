import { NgModule } from "@angular/core";

import { AccountSettingRoutingModule } from "./account-setting-routing.module";
import { AccountSettingComponent } from "./account-setting.component";
import { SharedModule } from "../../shared/module/shared.module";
import { CoreModule } from "../../core/core.module";
import { AddressComponent } from "./address/address.component";
import { AddressEditComponent } from "./address-edit/address-edit.component";
import { AddressCreateComponent } from "./address-create/address-create.component";
import { UserComponent } from "./user/user.component";
import { UserEditComponent } from "./user-edit/user-edit.component";
import { PasswordComponent } from "./password/password.component";
import { OrderComponent } from "./order/order.component";
import { OrderProductComponent } from "./order-product/order-product.component";
import { ReviewComponent } from "./review/review.component";
import { ReviewEditComponent } from "./review-edit/review-edit.component";
import { ReviewCreateComponent } from "./review-create/review-create.component";
import { AccountMenuComponent } from "./account-menu/account-menu.component";


@NgModule({
  declarations: [
    AccountMenuComponent,
    AccountSettingComponent,
    AddressComponent,
    AddressEditComponent,
    AddressCreateComponent,
    UserComponent,
    UserEditComponent,
    PasswordComponent,
    OrderComponent,
    OrderProductComponent,
    ReviewComponent,
    ReviewEditComponent,
    ReviewCreateComponent,
  ],
  imports: [
    SharedModule,
    CoreModule,
    AccountSettingRoutingModule,
  ],
  exports: [
    AccountMenuComponent,
  ]
})
export class AccountSettingModule {
}
