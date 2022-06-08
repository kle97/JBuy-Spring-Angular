import { Inject, Injectable } from "@angular/core";
import { DOCUMENT } from "@angular/common";

@Injectable({
  providedIn: "root",
})
export class ScrollUpService {

  constructor(
    @Inject(DOCUMENT) private document: Document,
  ) {
  }

  toTop(elementId: string, distance: number = 0) {
    let content: HTMLElement | null = this.document.getElementById(elementId);
    if (!content) {
      content = this.document.querySelector(elementId);
    }
    if (content) {
      content.scrollTop = distance;
    }
  }

  scrollToTop(elementId: string) {
    let content: HTMLElement | null = this.document.getElementById(elementId);
    if (!content) {
      content = this.document.querySelector(elementId);
    }
    if (content) {
      content.scroll({
        top: 0,
        left: 0,
        behavior: "smooth",
      });
    }
  }

  scrollIntoView(elementId: string, behavior: "auto" | "smooth" = "auto") {
    let content: HTMLElement | null = this.document.getElementById(elementId);
    if (!content) {
      content = this.document.querySelector(elementId);
    }
    if (content) {
      content.scrollIntoView({behavior: behavior});
    }
  }
}
