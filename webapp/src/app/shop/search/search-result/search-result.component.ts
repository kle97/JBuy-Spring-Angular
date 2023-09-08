import { ChangeDetectorRef, Component, OnInit, TemplateRef, TrackByFunction } from "@angular/core";
import { defaultFilterOption, FilterOption, ProductService } from "../repository/product.service";
import { ActivatedRoute, ParamMap } from "@angular/router";
import { UnsubscribeComponent } from "../../../core/component/unsubscribe/unsubscribe.component";
import { FacetPage } from "../model/facet-page.model";
import { Product } from "../model/product.model";
import { PageRequest } from "../../../core/model/page-request.model";
import { defaultPageRequest } from "../../../core/constant/default-page-request";
import { KeyValue, Location } from "@angular/common";
import { Facet } from "../model/facet.model";
import { emptyPage } from "../../../core/constant/empty-page";
import { MatDialog } from "@angular/material/dialog";
import { NotificationService } from "../../../core/service/notification.service";
import { PageEvent } from "@angular/material/paginator";
import { ScrollUpService } from "../../../core/service/scroll-up.service";
import { ShoppingCartService } from "../../shopping-cart/repository/shopping-cart.service";
import { takeUntil } from "rxjs";
import { HttpParams } from "@angular/common/http";
import { CustomHttpParamsEncoderService } from "../../../core/service/custom-http-params-encoder.service";
import { Title } from "@angular/platform-browser";

export const emptyFacetMap = new Map<string, Facet[]>();

export const emptyFacetPage: FacetPage<any> = {
  ...emptyPage,
  facetMap: emptyFacetMap,
};

@Component({
  selector: "app-search-result",
  templateUrl: "./search-result.component.html",
  styleUrls: ["./search-result.component.scss"],
})
export class SearchResultComponent extends UnsubscribeComponent implements OnInit {

  searchTextParam: string = "";
  httpParams!: HttpParams;

  math = Math;

  sortOptions: Array<string> = ["", "price", "price,desc", "rating,desc", "rating"];
  sortOptionLabels: Array<string> = ["Most Relevance", "Lowest Price", "Highest Price", "Highest rating", "Lowest rating"];
  selectedSort: string = "";

  productResultPage: FacetPage<Product> = emptyFacetPage;
  pageRequest: PageRequest = defaultPageRequest;
  facetMap: Map<string, Facet[]> = new Map<string, Facet[]>(JSON.parse(JSON.stringify([...emptyFacetMap])));
  filterOption: FilterOption = JSON.parse(JSON.stringify(defaultFilterOption));
  noContent: boolean = false;

  constructor(
    private titleService: Title,
    private productService: ProductService,
    private shoppingCartService: ShoppingCartService,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private customHttpParamsEncoderService: CustomHttpParamsEncoderService,
    private notificationService: NotificationService,
    private scrollUpService: ScrollUpService,
    private changeDetectionRef: ChangeDetectorRef,
    private location: Location,
  ) {
    super();
  }

  ngOnInit(): void {
    this.activatedRoute.queryParamMap.pipe(takeUntil(this.unsubscribe$)).subscribe(params => {
      const tempParam = params.get("searchText");
      this.searchTextParam = tempParam ? tempParam : "";
      if (!this.searchTextParam) {
        return;
      }
      this.titleService.setTitle("JBuy: " + this.searchTextParam);
      this.initialize(params);
      this.search();
    });
  }

  initialize(params: ParamMap) {
    this.noContent = false;
    this.productResultPage = emptyFacetPage;
    this.facetMap = new Map<string, Facet[]>(JSON.parse(JSON.stringify([...emptyFacetMap])));
    this.retrieveParam(params);
    this.httpParams = new HttpParams({ encoder: this.customHttpParamsEncoderService });
  }

  retrieveParam(params: ParamMap) {
    let tempParam: string | null = params.get("page");
    const tempPage = tempParam ? parseInt(tempParam) - 1 : 0;
    this.pageRequest.page = tempPage > 0 ? tempPage : 0;
    tempParam = params.get("size");
    this.pageRequest.size = tempParam ? parseInt(tempParam) : 20;
    const tempSortParam = params.get("sort");
    this.selectedSort = tempSortParam ? tempSortParam : "";
    this.pageRequest.sort = this.selectedSort ? [this.selectedSort] : [];
    tempParam = params.get("dealFilter");
    this.filterOption.dealFilter = tempParam === "true";
    tempParam = params.get("categoryFilter");
    this.filterOption.categoryFilter = tempParam ? tempParam : "";
    this.filterOption.brandFilter = params.getAll("brandFilter");
    this.filterOption.priceFilter = params.getAll("priceFilter");
    tempParam = params.get("rating");
    this.filterOption.ratingFilter = tempParam ? parseInt(tempParam) : 0;
    this.filterOption.attributeFilter = params.getAll("attributeFilter");
  }

  replaceLocationState() {
    this.location.go("/search", this.httpParams.toString());
  }

  replaceAllUrlParams() {
    this.updateUrlPageRequestParams();
    this.updateUrlFilterParams();
    this.replaceLocationState();
  }

  replaceUrlPageRequestParams() {
    this.updateUrlPageRequestParams();
    this.replaceLocationState();
  }

  replaceUrlFilterParams() {
    this.updateUrlFilterParams();
    this.replaceLocationState();
  }

  updateUrlPageRequestParams() {
    this.httpParams = this.httpParams.set("searchText", this.searchTextParam);
    this.httpParams = this.httpParams.delete("page");
    this.httpParams = this.httpParams.delete("size");
    this.httpParams = this.httpParams.delete("sort");
    if (this.pageRequest.page > 0) {
      this.httpParams = this.httpParams.set("page", this.pageRequest.page + 1);
    }
    if (this.pageRequest.size !== 20) {
      this.httpParams = this.httpParams.set("size", this.pageRequest.size);
    }
    if (this.pageRequest.sort.length > 0) {
      this.httpParams = this.httpParams.set("sort", this.pageRequest.sort[0]);
    }
  }

  updateUrlFilterParams() {
    this.httpParams = this.httpParams.set("searchText", this.searchTextParam);
    this.httpParams = this.httpParams.delete("dealFilter");
    this.httpParams = this.httpParams.delete("categoryFilter");
    this.httpParams = this.httpParams.delete("brandFilter");
    this.httpParams = this.httpParams.delete("priceFilter");
    this.httpParams = this.httpParams.delete("ratingFilter");
    this.httpParams = this.httpParams.delete("attributeFilter");
    if (this.filterOption.dealFilter) {
      this.httpParams = this.httpParams.set("dealFilter", this.filterOption.dealFilter);
    }
    if (this.filterOption.categoryFilter) {
      this.httpParams = this.httpParams.set("categoryFilter", this.filterOption.categoryFilter);
    }
    if (this.filterOption.brandFilter && this.filterOption.brandFilter.length > 0) {
      for (let brand of this.filterOption.brandFilter) {
        this.httpParams = this.httpParams.append("brandFilter", brand);
      }
    }
    if (this.filterOption.priceFilter && this.filterOption.priceFilter.length > 0) {
      for (let price of this.filterOption.priceFilter) {
        this.httpParams = this.httpParams.append("priceFilter", price);
      }
    }
    if (this.filterOption.ratingFilter && this.filterOption.ratingFilter > 0) {
      this.httpParams = this.httpParams.set("ratingFilter", this.filterOption.ratingFilter);
    }
    if (this.filterOption.attributeFilter && this.filterOption.attributeFilter.length > 0) {
      for (let attribute of this.filterOption.attributeFilter) {
        this.httpParams = this.httpParams.append("attributeFilter", attribute);
      }
    }
  }

  search() {
    if (this.searchTextParam) {
      this.productService.search(this.searchTextParam, this.pageRequest, this.filterOption).subscribe(productResultPage => {
        this.productResultPage = productResultPage;
        if (this.productResultPage.content.length > 0) {
          this.facetMap.clear();
          for (let [key, value] of Object.entries(this.productResultPage.facetMap)) {
            this.facetMap.set(key, value);
          }
          this.scrollUpService.scrollIntoView("body");
        } else {
          this.noContent = true;
        }
      });
    } else {
      this.noContent = true;
    }
  }

  addToCart(product: Product) {
    this.shoppingCartService.addCartItem({
      userId: "",
      productId: product.id,
      name: product.name,
      images: product.images,
      regularPrice: product.regularPrice,
      discountPrice: product.discountPrice,
      quantity: 1,
      stock: product.stock,
    });
  }

  onCompare(a: KeyValue<string, Facet[]>, b: KeyValue<string, Facet[]>): number {
    return 1;
  }

  trackAttribute: TrackByFunction<Facet> = (index: number, attribute: Facet): string => {
    return attribute.value;
  };

  trackFacetKeyValue: TrackByFunction<KeyValue<string, Facet[]>> = (index: number,
                                                                    keyValue: KeyValue<string, Facet[]>): string => {
    return keyValue.key;
  };

  trackProduct: TrackByFunction<Product> = (index: number, product: Product): string => {
    return product.id;
  };

  openFilterModal(templateRef: TemplateRef<any>) {
    this.dialog.open(templateRef, {
      width: "80rem",
      disableClose: true,
    });
  }

  onPageEvent(pageEvent: PageEvent) {
    this.pageRequest = {
      page: pageEvent.pageIndex,
      size: pageEvent.pageSize,
      sort: this.selectedSort ? [this.selectedSort] : [],
    };
    this.search();
    this.replaceUrlPageRequestParams();
  }

  sortChange() {
    this.pageRequest = {
      ...this.pageRequest,
      sort: this.selectedSort ? [this.selectedSort] : [],
    };
    this.pageRequest.page = 0;
    this.search();
    this.replaceUrlPageRequestParams();
  }

  onCheckBoxChange(attributeType: string, attribute: string, checked: boolean) {
    if (this.searchTextParam) {
      this.pageRequest.page = 0;
      this.onFilterChange(attributeType, attribute, checked);
      this.search();
      this.replaceAllUrlParams();
    }
  }

  removeChip(attributeType: string, attribute: string) {
    this.onCheckBoxChange(attributeType, attribute, false);
  }

  onFilterChange(attributeType: string, attribute: string, checked: boolean) {
    if (attributeType === "Deals") {
      this.filterOption.dealFilter = checked;
    } else if (attributeType === "Category") {
      this.filterOption.categoryFilter = checked ? attribute : "";
    } else if (attributeType === "Brand") {
      if (checked) {
        this.filterOption.brandFilter?.push(attribute);
      } else {
        this.filterOption.brandFilter = this.filterOption.brandFilter?.filter(value => value !== attribute);
      }
    } else if (attributeType === "Price") {
      const price: string = attribute.replace(/\$(\d+)( to | and Up)\$*(\d+)*/g, "$1-$3");
      if (checked) {
        this.filterOption.priceFilter?.push(price);
      } else {
        this.filterOption.priceFilter = this.filterOption.priceFilter?.filter(value => value !== price);
      }
    } else if (attributeType === "Rating") {
      const rating: number = +attribute.replace(/\D/g, "");
      this.filterOption.ratingFilter = checked ? rating : 0;
    } else {
      if (checked) {
        this.filterOption.attributeFilter?.push(attribute);
      } else {
        this.filterOption.attributeFilter = this.filterOption.attributeFilter?.filter(val => val !== attribute);
      }
    }
  }

  isFilterOn(): boolean {
    return this.filterOption.categoryFilter !== ""
      || (this.filterOption.brandFilter && this.filterOption.brandFilter.length > 0)
      || (this.filterOption.priceFilter && this.filterOption.priceFilter.length > 0)
      || (this.filterOption.ratingFilter && this.filterOption.ratingFilter > 0)
      || (this.filterOption.attributeFilter != undefined && this.filterOption.attributeFilter.length > 0);
  }

  clearAllFilter() {
    this.filterOption = JSON.parse(JSON.stringify(defaultFilterOption));
    this.pageRequest.page = 0;
    this.search();
    this.replaceAllUrlParams();
  }

}
