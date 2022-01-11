import { NgModule } from "@angular/core";

import { CoreRoutingModule } from "./core-routing.module";
import { SharedModule } from "../shared/module/shared.module";


@NgModule({
  declarations: [],
  imports: [
    SharedModule,
    CoreRoutingModule,
  ],
})
export class CoreModule {
}
