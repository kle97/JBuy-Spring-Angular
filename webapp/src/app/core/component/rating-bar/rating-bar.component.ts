import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";

@Component({
  selector: "app-rating-bar",
  templateUrl: "./rating-bar.component.html",
  styleUrls: ["./rating-bar.component.scss"],
})
export class RatingBarComponent implements OnInit {

  touched: boolean = false;
  hovering: boolean = false;

  @Output() selectedStarEvent = new EventEmitter<number>();
  @Input() selectedStar: number = 0;
  ratingText: string = "";

  oneStar: boolean = false;
  twoStar: boolean = false;
  threeStar: boolean = false;
  fourStar: boolean = false;
  fiveStar: boolean = false;

  constructor() {
  }

  ngOnInit(): void {
    this.noHover();
  }

  noHover() {
    if (this.selectedStar === 0) {
      this.ratingText = "";
      this.oneStar = false;
      this.twoStar = false;
      this.threeStar = false;
      this.fourStar = false;
      this.fiveStar = false;
    } else if (this.selectedStar === 1) {
      this.hoverOne();
    } else if (this.selectedStar === 2) {
      this.hoverTwo();
    } else if (this.selectedStar === 3) {
      this.hoverThree();
    } else if (this.selectedStar === 4) {
      this.hoverFour();
    } else if (this.selectedStar === 5) {
      this.hoverFive();
    }
  }

  hoverOne() {
    this.ratingText = "Poor";
    this.oneStar = true;
    this.twoStar = false;
    this.threeStar = false;
    this.fourStar = false;
    this.fiveStar = false;
  }

  hoverTwo() {
    this.ratingText = "Fair";
    this.oneStar = true;
    this.twoStar = true;
    this.threeStar = false;
    this.fourStar = false;
    this.fiveStar = false;
  }

  hoverThree() {
    this.ratingText = "Average";
    this.oneStar = true;
    this.twoStar = true;
    this.threeStar = true;
    this.fourStar = false;
    this.fiveStar = false;
  }

  hoverFour() {
    this.ratingText = "Good";
    this.oneStar = true;
    this.twoStar = true;
    this.threeStar = true;
    this.fourStar = true;
    this.fiveStar = false;
  }

  hoverFive() {
    this.ratingText = "Excellent";
    this.oneStar = true;
    this.twoStar = true;
    this.threeStar = true;
    this.fourStar = true;
    this.fiveStar = true;
  }

  selectStar(star: number) {
    this.selectedStar = star;
    this.selectedStarEvent.emit(this.selectedStar);
  }

  onEnter() {
    this.touched = true;
    this.hovering = true;
  }

  onLeave() {
    this.hovering = false;
  }

}
