import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { httpInterceptorProviders } from "./core/interceptor";
import { MaterialModule } from "./shared/module/material.module";
import { HttpClientModule } from "@angular/common/http";
import { CookieModule } from "ngx-cookie";
import { CoreModule } from "./core/core.module";

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
    CookieModule.withOptions(),
    CoreModule,
    AppRoutingModule,
  ],
  providers: [
    httpInterceptorProviders,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
}
