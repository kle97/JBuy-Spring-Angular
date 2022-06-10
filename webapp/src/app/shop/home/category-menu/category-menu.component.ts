import { Component, OnInit, TrackByFunction } from "@angular/core";
import { CategoryService } from "../repository/category.service";
import { CategoryRepository } from "../repository/category.repository";
import { Observable } from "rxjs";
import { Category } from "../../product/model/category.model";
import { Page } from "../../../core/model/page.model";

@Component({
  selector: "app-category-menu",
  templateUrl: "./category-menu.component.html",
  styleUrls: ["./category-menu.component.scss"],
})
export class CategoryMenuComponent implements OnInit {

  categoryPage$: Observable<Page<Category>> = this.categoryRepository.categoryPage$;

  constructor(
    private categoryService: CategoryService,
    private categoryRepository: CategoryRepository,
  ) {
  }

  ngOnInit(): void {
    this.categoryRepository.isCategoryFetched$.subscribe(isCategoryPageFetched => {
      if (!isCategoryPageFetched) {
        this.categoryService.getCategoryPage();
      }
    });
  }

  trackCategory: TrackByFunction<Category> = (index: number, category: Category): string => {
    return category.id;
  };

}
