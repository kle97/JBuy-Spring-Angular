import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LoginComponent } from "./component/login/login.component";
import { AuthGuard } from "./guard/auth.guard";
import { SignUpComponent } from "./component/sign-up/sign-up.component";

const routes: Routes = [
  { path: "login", component: LoginComponent, canActivate: [AuthGuard], data: { title: "JBuy - Sign In" } },
  { path: "signup", component: SignUpComponent, canActivate: [AuthGuard], data: { title: "JBuy - Sign Up" }  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CoreRoutingModule {
}
