import { NgModule } from '@angular/core';

import { SearchRoutingModule } from './search-routing.module';
import { SharedModule } from "../../shared/module/shared.module";
import { CoreModule } from "../../core/core.module";


@NgModule({
  declarations: [],
  imports: [
    SharedModule,
    CoreModule,
    SearchRoutingModule
  ]
})
export class SearchModule { }
