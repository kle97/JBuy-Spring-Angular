import { Component, OnInit, TrackByFunction } from "@angular/core";
import { CategoryService } from "../../home/repository/category.service";
import { CategoryRepository } from "../../home/repository/category.repository";
import { debounceTime, distinctUntilChanged, filter, map, Observable, switchMap, takeUntil } from "rxjs";
import { Page } from "../../../core/model/page.model";
import { Category } from "../../product/model/category.model";
import { FormBuilder, FormControl } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { UnsubscribeComponent } from "../../../core/component/unsubscribe/unsubscribe.component";
import { RecentlySearchedTextRepository, SearchText } from "../repository/recently-searched-text.repository";
import { ProductService } from "../repository/product.service";

@Component({
  selector: "app-search-bar",
  templateUrl: "./search-bar.component.html",
  styleUrls: ["./search-bar.component.scss"],
})
export class SearchBarComponent extends UnsubscribeComponent implements OnInit {

  recentlySearchedTexts$: Observable<Array<SearchText>> = this.recentlySearchedTextRepository.recentlySearchedTexts$;
  categoryPage$: Observable<Page<Category>> = this.categoryRepository.categoryPage$;
  autocompleteSearchTexts$!: Observable<Array<string>>;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private categoryService: CategoryService,
    private productService: ProductService,
    private recentlySearchedTextRepository: RecentlySearchedTextRepository,
    private categoryRepository: CategoryRepository,
    private formBuilder: FormBuilder,
  ) {
    super();
  }

  ngOnInit(): void {
    this.categoryRepository.isCategoryFetched$.subscribe(isCategoryPageFetched => {
      if (!isCategoryPageFetched) {
        this.categoryService.getCategoryPage();
      }
    });

    this.activatedRoute.queryParamMap.pipe(takeUntil(this.unsubscribe$)).subscribe(params => {
      let tempParam = params.get("searchText");
      this.searchText.setValue(tempParam ? tempParam : "");
      tempParam = params.get("categoryFilter");
      this.category.setValue(tempParam ? tempParam : "All");
    });

    this.autocompleteSearchTexts$ = this.searchText.valueChanges.pipe(
      map(searchText => searchText as string),
      filter(searchText => searchText.length > 1),
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(searchText => this.productService.autocomplete(searchText).pipe(
        map(autocompleteTexts => autocompleteTexts.filter(text => text.length <= searchText.length + 20)),
        map(autocompleteTexts => autocompleteTexts.map(text => text.toLowerCase())),
      )),
    );
  }

  searchForm = this.formBuilder.group({
    searchText: [""],
    category: ["All"],
  });

  get searchText() {
    return this.searchForm.get("searchText") as FormControl;
  }

  get category() {
    return this.searchForm.get("category") as FormControl;
  }

  onSubmit() {
    if (this.searchText.value) {
      if (this.category.value !== "All") {
        this.router.navigate(["/search"], {
          queryParams: {
            searchText: this.searchText.value,
            categoryFilter: this.category.value,
          },
        });
        this.recentlySearchedTextRepository.addSearchText(this.searchText.value, this.category.value);
      } else {
        this.router.navigate(["/search"], { queryParams: { searchText: this.searchText.value } });
        this.recentlySearchedTextRepository.addSearchText(this.searchText.value);
      }
    }
  }

  removeSearchText(searchText: string, category: string) {
    this.recentlySearchedTextRepository.removeSearchText(searchText, category);
  }

  onRecentlySearchClick(category: string, input: HTMLInputElement) {
    if (category !== "All") {
      this.category.setValue(category);
    }
    setTimeout(() => {
      this.onSubmit();
      input.blur();
    });
  }

  trackCategory: TrackByFunction<Category> = (index: number, category: Category): string => {
    return category.id;
  };

  trackSearchText: TrackByFunction<SearchText> = (index: number, searchText: SearchText): string => {
    return searchText.id;
  };
}
