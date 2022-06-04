import { NgModule } from "@angular/core";

import { SearchRoutingModule } from "./search-routing.module";
import { SharedModule } from "../../shared/module/shared.module";
import { CoreModule } from "../../core/core.module";
import { SearchBarComponent } from "./search-bar/search-bar.component";
import { SearchResultComponent } from "./search-result/search-result.component";


@NgModule({
    declarations: [
        SearchBarComponent,
        SearchResultComponent,
    ],
    exports: [
        SearchBarComponent,
    ],
    imports: [
        SharedModule,
        CoreModule,
        SearchRoutingModule,
    ],
})
export class SearchModule { }
