import {
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  TrackByFunction,
  ViewChild,
} from "@angular/core";
import KeenSlider, { KeenSliderInstance } from "keen-slider";
import { RecentlyVisitedProductsRepository } from "../repository/recently-visited-products.repository";
import { Subject, takeUntil } from "rxjs";
import { Product } from "../../search/model/product.model";
import { ProductDetailService } from "../repository/product-detail.service";

@Component({
  selector: "app-recently-visited",
  templateUrl: "./recently-visited.component.html",
  styleUrls: [
    "../../../../../node_modules/keen-slider/keen-slider.min.css",
    "./recently-visited.component.scss",
  ],
})
export class RecentlyVisitedComponent implements OnInit, OnDestroy, OnChanges {

  @ViewChild("sliderRef") sliderRef!: ElementRef<HTMLElement>;
  @Input() productId: string = "";

  protected unsubscribe$ = new Subject<void>();
  currentSlide: number = 0;
  perView: number = 1;
  slider!: KeenSliderInstance;

  products!: Product[];
  productSlugs: string[] = [];

  constructor(
    private productDetailRepository: RecentlyVisitedProductsRepository,
    private productDetailService: ProductDetailService,
  ) {
  }

  ngOnInit(): void {
    this.productDetailRepository.recentlyVisitedProducts$
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((products) => {
        this.products = products.reverse();
        this.products.map(product => {
          const productName = product.name.split(";");
          this.productSlugs.push(this.productDetailService.slugify(productName[0]));
        });``
        if (this.products.length > 0) {
          setTimeout(() => {
            this.slider = new KeenSlider(this.sliderRef.nativeElement, {
              initial: this.currentSlide,
              drag: true,
              created: () => {
                setTimeout(() => {
                  this.perView = Math.floor(1 / this.slider.track.details.slides[this.currentSlide].size);
                });
              },
              updated: () => {
                setTimeout(() => {
                  this.slider.moveToIdx(0);
                  this.currentSlide = 0;
                  this.perView = Math.floor(1 / this.slider.track.details.slides[this.currentSlide].size);
                });
              },
              slideChanged: (s) => {
                this.currentSlide = s.track.details.rel;
              },
              breakpoints: {
                "(min-width: 405px)": {
                  slides: { perView: 2, spacing: 10 },
                },
                "(min-width: 610px)": {
                  slides: { perView: 3, spacing: 10 },
                },
                "(min-width: 815px)": {
                  slides: { perView: 4, spacing: 10 },
                  drag: false,
                },
                "(min-width: 1020px)": {
                  slides: { perView: 5, spacing: 10 },
                  drag: false,
                },
                "(min-width: 1225px)": {
                  slides: { perView: 6, spacing: 10 },
                  drag: false,
                },
              },
              slides: {
                perView: 2,
                spacing: 10,
              },
            });
          });
        }
      });
  }

  next(length: number) {
    return Math.min(this.currentSlide + this.perView, length - this.perView);
  }

  prev() {
    return Math.max(this.currentSlide - this.perView, 0);
  }

  ngOnDestroy() {
    if (this.slider) {
      this.slider.destroy();
    }
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!changes["productId"].firstChange && changes["productId"].currentValue === "") {
      this.slider.moveToIdx(0);
    }
  }

  trackProduct: TrackByFunction<Product> = (index: number, product: Product): string => {
    return product.id;
  };

}
