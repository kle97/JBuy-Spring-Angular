import { NgModule } from "@angular/core";

import { AdminRoutingModule } from "./admin-routing.module";
import { SharedModule } from "../shared/module/shared.module";


@NgModule({
  declarations: [],
  imports: [
    SharedModule,
    AdminRoutingModule,
  ],
})
export class AdminModule {
}
