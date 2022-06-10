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
import { BehaviorSubject } from "rxjs";
import KeenSlider, { KeenSliderInstance } from "keen-slider";
import { Product } from "../../search/model/product.model";
import { ProductService } from "../../search/repository/product.service";

@Component({
  selector: "app-similar-product",
  templateUrl: "./similar-product.component.html",
  styleUrls: [
    "../../../../../node_modules/keen-slider/keen-slider.min.css",
    "./similar-product.component.scss",
  ],
})
export class SimilarProductComponent implements OnInit, OnDestroy, OnChanges {

  @ViewChild("sliderRef") sliderRef!: ElementRef<HTMLElement>;
  @Input() searchText: string = "monitor";
  @Input() productId: string = "";

  currentSlide: number = 0;
  perView: number = 1;
  slider!: KeenSliderInstance;
  loaded: Array<boolean> = [true];
  loaded$: BehaviorSubject<Array<boolean>> = new BehaviorSubject<Array<boolean>>([true]);

  products: Product[] = [];
  productSlugs: string[] = [];

  constructor(
    private productService: ProductService,
  ) {
  }

  ngOnInit(): void {
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
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["searchText"].currentValue !== changes["searchText"].previousValue) {
      this.searchText = changes["searchText"].currentValue;
      if (this.searchText) {
        this.load();
      }
    }
    if (!changes["searchText"].firstChange && changes["searchText"].currentValue === "") {
      this.slider.moveToIdx(0);
    }
  }

  load() {
    this.productService.searchSimilarProduct(this.searchText)
      .subscribe((productPage) => {
        this.products = productPage.content.filter(product => product.id !== this.productId);
        this.products.map(product => {
          const productName = product.name.split(";");
          this.productSlugs.push(this.productService.slugify(productName[0]));
        });
        if (this.products.length > 0) {
          setTimeout(() => {
            this.slider = new KeenSlider(this.sliderRef.nativeElement, {
              initial: this.currentSlide,
              drag: true,
              created: (s) => {
                setTimeout(() => {
                  this.perView = Math.floor(1 / this.slider.track.details.slides[this.currentSlide].size);
                  this.changeLoaded(s);
                });
              },
              updated: (s) => {
                setTimeout(() => {
                  this.slider.moveToIdx(0);
                  this.currentSlide = 0;
                  this.perView = Math.floor(1 / this.slider.track.details.slides[this.currentSlide].size);
                  this.changeLoaded(s);
                });
              },
              slideChanged: (s) => {
                this.currentSlide = s.track.details.rel;
              },
              animationEnded: (s) => {
                this.changeLoaded(s);
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

  changeLoaded(s: KeenSliderInstance) {
    const idx = s.track.details.rel;
    for (let i = idx; i <= idx + this.perView; i++) {
      this.loaded[i] = true;
    }
    this.loaded$.next(this.loaded);
  }

  trackProduct: TrackByFunction<Product> = (index: number, product: Product): string => {
    return product.id;
  };

}
