import { NgModule } from "@angular/core";

import { CoreRoutingModule } from "./core-routing.module";
import { SharedModule } from "../shared/module/shared.module";
import { MatchMediaPipe } from "./pipe/match-media.pipe";
import { LoadingPipe } from "./pipe/loading.pipe";
import { VarDirective } from "./directive/var.directive";
import { LoginComponent } from "./component/login/login.component";
import { LogoutComponent } from "./component/logout/logout.component";
import { LoadingComponent } from "./component/loading/loading.component";
import { PageNotFoundComponent } from "./component/page-not-found/page-not-found.component";
import { NavigationBarComponent } from "./component/navigation-bar/navigation-bar.component";
import { SignUpComponent } from "./component/sign-up/sign-up.component";
import { RatingComponent } from "./component/rating/rating.component";
import { ConfirmDialogComponent } from "./component/confirm-dialog/confirm-dialog.component";
import { PaginatorComponent } from "./component/paginator/paginator.component";
import { RatingBarComponent } from "./component/rating-bar/rating-bar.component";
import { FooterComponent } from "./component/footer/footer.component";
import { LoadingSvgComponent } from "./component/loading-svg/loading-svg.component";
import { ScrollUpButtonComponent } from "./component/scroll-up-button/scroll-up-button.component";
import { HighlightTextPipe } from './pipe/highlight-text.pipe';


@NgModule({
  declarations: [
    MatchMediaPipe,
    LoadingPipe,
    VarDirective,
    LoginComponent,
    LogoutComponent,
    LoadingComponent,
    PageNotFoundComponent,
    NavigationBarComponent,
    SignUpComponent,
    RatingComponent,
    ConfirmDialogComponent,
    PaginatorComponent,
    RatingBarComponent,
    FooterComponent,
    LoadingSvgComponent,
    ScrollUpButtonComponent,
    HighlightTextPipe,
  ],
  imports: [
    SharedModule,
    CoreRoutingModule,
  ],
  exports: [
    VarDirective,
    MatchMediaPipe,
    LoadingPipe,
    HighlightTextPipe,
    LogoutComponent,
    LoadingComponent,
    PageNotFoundComponent,
    NavigationBarComponent,
    RatingComponent,
    RatingBarComponent,
    PaginatorComponent,
    FooterComponent,
    LoadingSvgComponent,
    ScrollUpButtonComponent
  ],
})
export class CoreModule {
}
