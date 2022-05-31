import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AccountSettingComponent } from "./account-setting.component";
import { AddressComponent } from "./address/address.component";
import { AuthGuard } from "../../core/guard/auth.guard";
import { UserComponent } from "./user/user.component";
import { PasswordComponent } from "./password/password.component";
import { OrderComponent } from "./order/order.component";
import { OrderProductComponent } from "./order-product/order-product.component";
import { ReviewComponent } from "./review/review.component";

const routes: Routes = [
  {
    path: "setting",
    component: AccountSettingComponent,
    children: [
      { path: "", redirectTo: "/account/setting/user", pathMatch: "full" },
      {
        path: "user",
        component: UserComponent,
        data: { title: "JBuy - Personal Information" },
        canActivate: [AuthGuard],
      },
      { path: "password", component: PasswordComponent, data: { title: "JBuy - Password" }, canActivate: [AuthGuard] },
      { path: "address", component: AddressComponent, data: { title: "JBuy - Addresses" }, canActivate: [AuthGuard] },
      {
        path: "order",
        component: OrderComponent,
        data: { title: "JBuy - Purchases and Orders" },
        canActivate: [AuthGuard],
        children: [
          {
            path: "order-product",
            component: OrderProductComponent,
            data: { title: "JBuy - Purchases and Orders" },
            canActivate: [AuthGuard],
          },
        ]
      },
      { path: "review", component: ReviewComponent, data: { title: "JBuy - Reviews" }, canActivate: [AuthGuard] },
    ],
  },
  { path: "account", redirectTo: "/account/setting/user", pathMatch: "full" },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AccountSettingRoutingModule {
}
