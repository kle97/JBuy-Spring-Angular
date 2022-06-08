import { Component, HostListener, OnInit } from "@angular/core";
import { ScrollUpService } from "../../service/scroll-up.service";

@Component({
  selector: "app-scroll-up-button",
  templateUrl: "./scroll-up-button.component.html",
  styleUrls: ["./scroll-up-button.component.scss"],
})
export class ScrollUpButtonComponent implements OnInit {

  constructor(
    private scrollUpService: ScrollUpService,
  ) {
  }

  showButton: boolean = false;

  ngOnInit(): void {
  }

  @HostListener("window:scroll", ["$event"])
  onWindowScroll() {
    this.showButton = window.scrollY >= 600;
  }

  onButtonClick() {
    setTimeout(() => this.scrollUpService.scrollIntoView("body", "smooth"));
  }

}
