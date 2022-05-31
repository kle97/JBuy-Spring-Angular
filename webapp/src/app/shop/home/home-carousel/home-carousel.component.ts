import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from "@angular/core";
import KeenSlider, { KeenSliderInstance } from "keen-slider";

@Component({
  selector: "app-home-carousel",
  templateUrl: "./home-carousel.component.html",
  styleUrls: [
    "../../../../../node_modules/keen-slider/keen-slider.min.css",
    "./home-carousel.component.scss",
  ],
})
export class HomeCarouselComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild("sliderRef") sliderRef!: ElementRef<HTMLElement>;
  slider!: KeenSliderInstance;
  currentSlide: number = 0;
  dotHelper: Array<Number> = [];
  timeout!: NodeJS.Timeout;
  autoSlide: boolean = true;

  constructor() {
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.slider = new KeenSlider(this.sliderRef.nativeElement, {
        initial: this.currentSlide,
        drag: true,
        loop: true,
        created: () => {
          this.nextTimeout();
        },
        dragStarted: () => {
          this.clearNextTimeOut();
        },
        animationEnded: () => {
          this.nextTimeout();
        },
        updated: () => {
          this.nextTimeout();
        },
        slideChanged: (s) => {
          this.currentSlide = s.track.details.rel;
        },
      });
      this.dotHelper = [
        ...Array(this.slider.track.details.slides.length).keys(),
      ];
    });
  }

  clearNextTimeOut() {
    clearTimeout(this.timeout);
  }

  nextTimeout() {
    clearTimeout(this.timeout);
    if (this.autoSlide) {
      this.timeout = setTimeout(() => {
        this.slider.next();
      }, 5000);
    }
  }

  toggleAutoSlide() {
    this.autoSlide = !this.autoSlide;
    if (this.autoSlide) {
      this.nextTimeout();
    } else {
      this.clearNextTimeOut();
    }
  }

  ngOnDestroy() {
    if (this.slider) {
      this.slider.destroy();
    }
  }


}
