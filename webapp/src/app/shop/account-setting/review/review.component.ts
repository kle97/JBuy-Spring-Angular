import { Component, OnInit, TrackByFunction } from "@angular/core";
import { Observable } from "rxjs";
import { Page } from "../../../core/model/page.model";
import { ScrollUpService } from "../../../core/service/scroll-up.service";
import { PageRequest } from "../../../core/model/page-request.model";
import { defaultPageRequest } from "../../../core/constant/default-page-request";
import { PageEvent } from "@angular/material/paginator";
import { Review } from "../model/review.model";
import { ReviewService } from "../repository/review/review.service";
import { ReviewRepository } from "../repository/review/review.repository";

@Component({
  selector: "app-review",
  templateUrl: "./review.component.html",
  styleUrls: ["./review.component.scss"],
})
export class ReviewComponent implements OnInit {

  reviewPage$: Observable<Page<Review>> = this.reviewRepository.reviewPage$;

  constructor(
    private reviewRepository: ReviewRepository,
    private reviewService: ReviewService,
    private scrollUpService: ScrollUpService,
  ) {
  }

  ngOnInit(): void {
    const pageRequest: PageRequest = {
      ...defaultPageRequest,
      sort: ["reviewDate,desc"],
    };

    this.reviewService.getReviewPageForUser(pageRequest);
  }

  editReview(review: Review) {
    this.reviewService.updateReview(review);
  }

  onPageEvent(pageEvent: PageEvent) {
    const pageRequest: PageRequest = {
      page: pageEvent.pageIndex,
      size: pageEvent.pageSize,
      sort: ["reviewDate,desc"],
    };

    this.reviewService.getReviewPageForUser(pageRequest);
    this.scrollUpService.toTop("body");
  }

  trackReview: TrackByFunction<Review> = (index: number, review: Review): string => {
    return review.userId + review.productId;
  };

}
