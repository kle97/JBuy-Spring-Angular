import { Page } from "../../../core/model/page.model";
import { emptyPage } from "../../../core/constant/empty-page";
import { createStore, withProps } from "@ngneat/elf";
import { Injectable } from "@angular/core";
import { Category } from "../../product/model/category.model";
import { localStorageStrategy, persistState } from "@ngneat/elf-persist-state";

export const initialCategoryPage: Page<Category> = emptyPage;

export const categoryStore = createStore({ name: "category" }, withProps<Page<Category>>(initialCategoryPage));

@Injectable({
  providedIn: "root",
})
export class CategoryRepository {
  categoryPage$ = categoryStore.pipe(state => state);

  setCategoryPage(categoryPage: Page<Category>) {
    categoryStore.update(() => ({ ...categoryPage }));
  }
}

export const categoryPersist = persistState(categoryStore, {
  key: "categories",
  storage: localStorageStrategy,
});
