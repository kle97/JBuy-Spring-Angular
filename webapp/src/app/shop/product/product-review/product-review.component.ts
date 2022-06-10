import { AfterViewInit, Component, Input, OnInit, TrackByFunction } from "@angular/core";
import { Observable } from "rxjs";
import { Page } from "../../../core/model/page.model";
import { Review } from "../../account-setting/model/review.model";
import { ReviewRepository } from "../../account-setting/repository/review/review.repository";
import { ReviewService } from "../../account-setting/repository/review/review.service";
import { ScrollUpService } from "../../../core/service/scroll-up.service";
import { PageRequest } from "../../../core/model/page-request.model";
import { defaultPageRequest } from "../../../core/constant/default-page-request";
import { PageEvent } from "@angular/material/paginator";

@Component({
  selector: 'app-product-review',
  templateUrl: './product-review.component.html',
  styleUrls: ['./product-review.component.scss']
})
export class ProductReviewComponent implements OnInit, AfterViewInit {

  reviewPage$: Observable<Page<Review>> = this.reviewRepository.reviewPage$;
  @Input() productId: string = "";
  @Input() ratingCount: number = 0;
  @Input() averageRating: number = 0;
  sortOptions: Array<string> = ["reviewDate,desc", "reviewDate", "rating,desc", "rating"];
  sortOptionLabels: Array<string> = ["Most Recent", "Oldest", "Highest Rating", "Lowest Rating"];
  selectedSort: string = "reviewDate,desc";

  constructor(
    private reviewRepository: ReviewRepository,
    private reviewService: ReviewService,
    private scrollUpService: ScrollUpService,
  ) {
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    const pageRequest: PageRequest = {
      ...defaultPageRequest,
      sort: ["reviewDate,desc"],
    };

    if (this.productId) {
      this.reviewService.getReviewPageForProduct(this.productId, pageRequest);
    }
  }

  sortChange(reviewPage: Page<Review>) {
    if (this.productId) {
      const pageRequest: PageRequest = {
        page: reviewPage.number,
        size: reviewPage.size,
        sort: [this.selectedSort],
      };
      this.reviewService.getReviewPageForProduct(this.productId, pageRequest);
      this.scrollUpService.toTop("product-review-tab");
    }
  }

  onPageEvent(pageEvent: PageEvent) {
    const pageRequest: PageRequest = {
      page: pageEvent.pageIndex,
      size: pageEvent.pageSize,
      sort: [this.selectedSort],
    };

    if (this.productId) {
      this.reviewService.getReviewPageForProduct(this.productId, pageRequest);
      this.scrollUpService.toTop("product-review-tab");
    }
  }

  trackReview: TrackByFunction<Review> = (index: number, review: Review): string => {
    return review.userId + review.productId;
  };

}
