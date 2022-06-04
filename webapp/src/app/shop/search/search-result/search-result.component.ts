import { Component, OnInit } from "@angular/core";
import { FilterOption, ProductService } from "../repository/product.service";
import { Observable, takeUntil } from "rxjs";
import { ActivatedRoute } from "@angular/router";
import { UnsubscribeComponent } from "../../../core/component/unsubscribe/unsubscribe.component";
import { FacetPage } from "../model/facet-page.model";
import { Product } from "../model/product.model";
import { PageRequest } from "../../../core/model/page-request.model";
import { defaultPageRequest } from "../../../core/constant/default-page-request";
import { ProductResultRepository } from "../repository/product-result.repository";

@Component({
  selector: "app-search-result",
  templateUrl: "./search-result.component.html",
  styleUrls: ["./search-result.component.scss"],
})
export class SearchResultComponent extends UnsubscribeComponent implements OnInit {

  searchText: string | null = "";
  category: string | null = "";
  productPage!: FacetPage<Product>;
  pageRequest: PageRequest = defaultPageRequest;
  productResultPage$: Observable<FacetPage<Product>> = this.productResultRepository.productResultPage$;

  constructor(
    private productService: ProductService,
    private activatedRoute: ActivatedRoute,
    private productResultRepository: ProductResultRepository,
  ) {
    super();
  }

  ngOnInit(): void {
    this.activatedRoute.queryParamMap.pipe(takeUntil(this.unsubscribe$)).subscribe(params => {
      this.searchText = params.get("searchText");
      this.category = params.get("category");
      if (this.searchText) {
        if (this.category) {
          const filterOption: FilterOption = {
            categoryFilter: this.category,
          };
          this.productService.search(this.searchText, this.pageRequest, filterOption);
        } else {
          this.productService.search(this.searchText, this.pageRequest);
        }
      }
    });
  }

}
