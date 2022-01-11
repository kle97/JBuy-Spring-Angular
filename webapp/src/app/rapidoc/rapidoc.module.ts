import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";

import { RapidocRoutingModule } from "./rapidoc-routing.module";
import { SharedModule } from "../shared/module/shared.module";
import { CoreModule } from "../core/core.module";
import { UiComponent } from "./ui/ui.component";
import "rapidoc";

@NgModule({
  declarations: [
    UiComponent,
  ],
  imports: [
    SharedModule,
    CoreModule,
    RapidocRoutingModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class RapidocModule {
}
