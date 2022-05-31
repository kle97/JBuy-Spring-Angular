import { NgModule } from "@angular/core";

import { CheckoutRoutingModule } from "./checkout-routing.module";
import { CheckoutComponent } from "./checkout.component";
import { SharedModule } from "../../shared/module/shared.module";
import { CoreModule } from "../../core/core.module";
import { CheckoutCompleteComponent } from './checkout-complete/checkout-complete.component';
import { BuyNowComponent } from './buy-now/buy-now.component';


@NgModule({
  declarations: [
    CheckoutComponent,
    CheckoutCompleteComponent,
    BuyNowComponent,
  ],
  imports: [
    SharedModule,
    CoreModule,
    CheckoutRoutingModule,
  ],
})
export class CheckoutModule {
}
