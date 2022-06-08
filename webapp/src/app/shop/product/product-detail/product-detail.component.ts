import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { ProductDetailService } from "../repository/product-detail.service";
import { Title } from "@angular/platform-browser";
import { ProductDetail } from "../model/product-detail.model";
import KeenSlider, { KeenSliderInstance, KeenSliderPlugin } from "keen-slider";
import { Subject, takeUntil } from "rxjs";
import { KeyValue, Location } from "@angular/common";
import { CartItem } from "../../shopping-cart/model/cart-item.model";
import { ShoppingCartService } from "../../shopping-cart/repository/shopping-cart.service";
import { BuyNowCartRepository } from "../../shopping-cart/repository/buy-now-cart.repository";
import { ScrollUpService } from "../../../core/service/scroll-up.service";

interface Attribute {
  name: string,
  value: string,
}

interface AttributeType {
  attributeTypeName: string,
  attributes: Array<Attribute>,
}

@Component({
  selector: "app-product-detail",
  templateUrl: "./product-detail.component.html",
  styleUrls: [
    "../../../../../node_modules/keen-slider/keen-slider.min.css",
    "./product-detail.component.scss",
  ],
})
export class ProductDetailComponent implements OnInit, OnDestroy {

  private unsubscribe$ = new Subject<void>();

  @ViewChild("sliderRef") sliderRef!: ElementRef<HTMLElement>;
  @ViewChild("thumbnailRef") thumbnailRef!: ElementRef<HTMLElement>;

  slider!: KeenSliderInstance;
  thumbnailSlider!: KeenSliderInstance;

  quantities: number[] = [];
  selectedQuantity: number = 1;
  name: string = "";
  subName: string = "";
  imageUrls: string[] = [];
  thumbnailUrls: string[] = [];
  productAttributes!: Map<string, Array<Attribute>>;
  selectedInfoTab: number = 0;
  similarProductSearchText: string = "";
  productId: string = "";
  loadReview: boolean = false;
  loadSpec: boolean = false;

  product: ProductDetail = {
    id: "",
    brand: "",
    name: "",
    description: "",
    regularPrice: 0,
    discountPrice: 0,
    stock: 0,
    images: "",
    thumbnails: "",
    averageRating: 0,
    ratingCount: 0,
    listOfCategory: [],
  };

  constructor(
    private titleService: Title,
    private productDetailService: ProductDetailService,
    private shoppingCartService: ShoppingCartService,
    private buyNowCartRepository: BuyNowCartRepository,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private location: Location,
    private scrollUpService: ScrollUpService,
  ) {
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap.pipe(takeUntil(this.unsubscribe$)).subscribe(params => {
      this.selectedInfoTab = 0;
      this.selectedQuantity = 1;
      this.loadReview = false;
      this.similarProductSearchText = "";
      this.productId = "";
      this.productAttributes = new Map();
      // this.scrollUpService.toTop("body");

      let id = params.get("id");
      const urlTitle = params.get("title");
      if (id) {
        this.productDetailService.getProduct(id).subscribe({
          next: (product) => {
            this.product = product;
            this.similarProductSearchText = this.product.listOfCategory[0].name;
            this.productId = this.product.id;

            const productName = this.product.name.split(";");
            if (productName.length > 1) {
              this.name = productName[0];
              productName.shift();
              this.subName = productName.join(";");
            } else {
              this.name = this.product.name;
            }

            const slugTitle = this.productDetailService.slugify(this.name);
            if (id && this.router.url.split("%23")[1]) {
              id = id.split("#")[0];
              setTimeout(() => this.clickRating(), 500);
            }

            if (!urlTitle || urlTitle !== slugTitle) {
              this.location.replaceState(`/product/${id}/${slugTitle}`, "", {});
              // this.router.navigate([`/product/${id}/${slugTitle}`], {skipLocationChange: true});
            }

            this.titleService.setTitle("JBuy: " + this.name);
            this.imageUrls = this.product.images.split("|");
            this.thumbnailUrls = this.product.thumbnails.split("|");

            this.quantities = this.product.stock < 30
              ? Array.from(Array(this.product.stock).keys()).map(i => i + 1)
              : Array.from(Array(30).keys()).map(i => i + 1);

            setTimeout(() => {
              this.slider = new KeenSlider(this.sliderRef.nativeElement);
              this.thumbnailSlider = new KeenSlider(
                this.thumbnailRef.nativeElement,
                {
                  initial: 0,
                  mode: "free",
                  slides: {
                    origin: "center",
                    perView: 4,
                    spacing: 5,
                  },
                },
                [this.thumbnailPlugin(this.slider)],
              );
            });
          },
          error: () => {
            this.router.navigateByUrl("**", { skipLocationChange: true });
          },
        });
      }
    });
  }

  clickRating() {
    this.scrollUpService.scrollIntoView("#product-info");
    this.selectedInfoTab = 2;
  }

  addToCart(product: ProductDetail) {
    this.shoppingCartService.addCartItem({
      userId: "",
      productId: product.id,
      name: product.name,
      images: product.images,
      regularPrice: product.regularPrice,
      discountPrice: product.discountPrice,
      quantity: this.selectedQuantity,
      stock: product.stock,
    });
  }

  tabChange(index: number) {
    if (index === 1 && this.productId && !this.loadSpec) {
      this.productDetailService.getProductAttributes(this.productId).subscribe(productAttributes => {
        productAttributes.map(productAttribute => {
          if (!this.productAttributes.has(productAttribute.attribute.attributeType)) {
            let attributes: Array<Attribute> = [];
            attributes.push({
              name: productAttribute.attribute.name,
              value: productAttribute.value,
            });
            this.productAttributes.set(productAttribute.attribute.attributeType, attributes);
          } else {
            this.productAttributes.get(productAttribute.attribute.attributeType)?.push({
              name: productAttribute.attribute.name,
              value: productAttribute.value,
            });
          }
        });

        this.loadSpec = true;
      })
    } else if (index === 2) {
      this.loadReview = true;
    }
  }

  buyNow(product: ProductDetail) {
    const cartItem: CartItem = {
      userId: "",
      productId: product.id,
      name: product.name,
      images: product.images,
      regularPrice: product.regularPrice,
      discountPrice: product.discountPrice,
      quantity: this.selectedQuantity,
      stock: product.stock,
    };
    this.buyNowCartRepository.setBuyNowItem(cartItem);
    this.shoppingCartService.addCartItem(cartItem, false);
    this.router.navigateByUrl("/buy-now");
  }

  onCompare(_left: KeyValue<any, any>, _right: KeyValue<any, any>): number {
    return 1;
  }

  ngOnDestroy() {
    if (this.slider) {
      this.slider.destroy();
    }
    if (this.thumbnailSlider) {
      this.thumbnailSlider.destroy();
    }
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  thumbnailPlugin = (main: KeenSliderInstance): KeenSliderPlugin => {
    return (slider) => {
      let activeNumber = 0;

      const removeActive = () => {
        slider.slides.forEach((slide) => {
          slide.classList.remove("active");
        });
      };

      const addActive = (idx: number) => {
        slider.slides[idx].classList.add("active");
      };

      const addClickEvents = () => {
        slider.slides.forEach((slide, idx) => {
          slide.addEventListener("click", () => {
            slider.slides[activeNumber].classList.remove("selected");
            activeNumber = idx;
            slider.slides[idx].classList.add("selected");
            main.moveToIdx(idx);
          });
        });
      };

      slider.on("created", () => {
        addActive(slider.track.details.rel);
        addClickEvents();
        main.on("animationStarted", (main) => {
          removeActive();
          const next = main.animator.targetIdx || 0;
          addActive(main.track.absToRel(next));
          slider.moveToIdx(next);
        });
      });
    };
  };

}
