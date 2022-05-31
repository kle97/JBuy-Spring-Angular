import { enableProdMode } from "@angular/core";
import { platformBrowserDynamic } from "@angular/platform-browser-dynamic";

import { AppModule } from "./app/app.module";
import { environment } from "./environments/environment";
import { devTools } from "@ngneat/elf-devtools";

if (environment.production) {
  enableProdMode();
} else {
  devTools();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
