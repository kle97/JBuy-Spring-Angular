import { Component, OnInit } from "@angular/core";
import { CategoryService } from "../../home/repository/category.service";
import { CategoryRepository } from "../../home/repository/category.repository";
import { Observable } from "rxjs";
import { Page } from "../../../core/model/page.model";
import { Category } from "../../product/model/category.model";

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent implements OnInit {

  categoryPage$: Observable<Page<Category>> = this.categoryRepository.categoryPage$;
  selectedCategory: string = "All";

  constructor(
    private categoryService: CategoryService,
    private categoryRepository: CategoryRepository,
  ) { }

  ngOnInit(): void {
    this.categoryRepository.isCategoryFetched$.subscribe(isCategoryPageFetched => {
      if (!isCategoryPageFetched) {
        this.categoryService.getCategoryPage();
      }
    });
  }

}
