import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
  {
    path: "rapidoc",
    loadChildren: () => import("./rapidoc/rapidoc.module").then(m => m.RapidocModule),
    data: { title: "Rapidoc" },
  },
  {
    path: "",
    loadChildren: () => import("./shop/shop.module").then(m => m.ShopModule),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    anchorScrolling: "enabled",
    scrollPositionRestoration: "enabled",
    onSameUrlNavigation: "reload",
  })],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
