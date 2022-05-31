import { Page } from "../../../../core/model/page.model";
import { emptyPage } from "../../../../core/constant/empty-page";
import { createStore, withProps } from "@ngneat/elf";
import { Injectable } from "@angular/core";
import { Review } from "../../model/review.model";

export const initialReviewPage: Page<Review> = emptyPage;

export const reviewStore = createStore({ name: "review" }, withProps<Page<Review>>(initialReviewPage));

@Injectable({
  providedIn: "root",
})
export class ReviewRepository {
  reviewPage$ = reviewStore.pipe(state => state);

  setReviewPage(reviewPage: Page<Review>) {
    reviewStore.update(() => ({ ...reviewPage }));
  }
}
