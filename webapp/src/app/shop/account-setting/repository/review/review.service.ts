import { Injectable } from "@angular/core";
import { AbstractGenericCrudService } from "../../../../core/service/generic-crud.service";
import { Review } from "../../model/review.model";
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { AuthenticationRepository } from "../../../../core/repository/authentication/authentication.repository";
import { ErrorNotificationService } from "../../../../core/service/error-notification.service";
import { ReviewRepository } from "./review.repository";
import { catchError, Observable, of, switchMap, take, throwError } from "rxjs";
import { PageRequest } from "../../../../core/model/page-request.model";
import { defaultPageRequest } from "../../../../core/constant/default-page-request";
import { Page } from "../../../../core/model/page.model";
import { MatDialog } from "@angular/material/dialog";
import { NotificationService } from "../../../../core/service/notification.service";
import { ReviewCreateComponent } from "../../review-create/review-create.component";
import { ReviewEditComponent } from "../../review-edit/review-edit.component";

@Injectable({
  providedIn: "root",
})
export class ReviewService extends AbstractGenericCrudService<Review, string> {

  constructor(
    protected override http: HttpClient,
    private authenticationRepository: AuthenticationRepository,
    private reviewRepository: ReviewRepository,
    private errorNotificationService: ErrorNotificationService,
    private dialog: MatDialog,
    private notificationService: NotificationService,
  ) {
    super(http, "/reviews", {
      readOneUrl: "/:id/products/:id",
      readPageUrl: "/users/:id/page",
      updateUrl: "/:id/products/:id",
      readAll: false,
      delete: false,
    });
  }

  private readonly productReviewPageUrl = "/products/:id/page";

  protected override handleError(errorResponse: HttpErrorResponse): Observable<never> {
    // this.errorNotificationService.open(errorResponse);
    return throwError(() => errorResponse);
  }

  getReviewPageForUser(pageRequest: PageRequest = defaultPageRequest) {
    this.authenticationRepository.user$.pipe(take(1))
      .subscribe(user => {
        if (user.expiry > 0 && Date.now() < user.expiry) {
          const httpParams: HttpParams = new HttpParams({
            fromObject: {
              page: pageRequest.page,
              size: pageRequest.size,
              sort: pageRequest.sort,
            },
          });

          this.readPage(httpParams, user.id).subscribe(reviewPage => {
            this.reviewRepository.setReviewPage(reviewPage);
          });
        }
      });
  }

  getReviewPageForProduct(productId: string, pageRequest: PageRequest = defaultPageRequest) {
    const httpParams: HttpParams = new HttpParams({
      fromObject: {
        page: pageRequest.page,
        size: pageRequest.size,
        sort: pageRequest.sort,
      },
    });

    const url = this.getUrlWithId(this.entityUrl + this.productReviewPageUrl, productId);

    this.http.get<Page<Review>>(url, { ...this.httpOptions, params: httpParams }).pipe(
      catchError(errorResponse => this.handleError(errorResponse)),
    ).subscribe(reviewPage => {
      this.reviewRepository.setReviewPage(reviewPage);
    });
  }

  createOrEditReview(productId: string) {
    this.authenticationRepository.user$.pipe(take(1))
      .subscribe(user => {
        if (user.expiry > 0 && Date.now() < user.expiry) {
          this.readOne(user.id, productId).subscribe({
            next: (review) => {
              this.updateReview(review);
            },
            error: (errorResponse) => {
              if (errorResponse instanceof HttpErrorResponse && errorResponse.status === 404) {
                this.createReview(productId);
              }
            },
          });
        }
      });
  }

  createReview(productId: string) {
    this.authenticationRepository.user$.pipe(take(1))
      .subscribe(user => {
        if (user.expiry > 0 && Date.now() < user.expiry) {
          const dialogRef = this.dialog.open(ReviewCreateComponent, { width: "70rem", disableClose: true });
          dialogRef.afterClosed().subscribe((formValue: Review) => {
            if (formValue) {
              this.create({
                userId: user.id,
                productId,
                title: formValue.title,
                comment: formValue.comment,
                rating: formValue.rating,
              }).subscribe(() => {
                this.notificationService.open("New Comment Created!", 2000);
                // this.getReviewPageForUser();
              });
            }
          });
        }
      });
  }

  updateReview(review: Review) {
    this.authenticationRepository.user$.pipe(take(1))
      .subscribe(user => {
        if (user.expiry > 0 && Date.now() < user.expiry) {
          const dialogRef = this.dialog.open(ReviewEditComponent, {
            data: { review: review },
            width: "70rem",
            disableClose: true,
          });
          dialogRef.afterClosed().subscribe((formValue: Review) => {
            if (formValue) {
              this.update({
                userId: user.id,
                productId: review.productId,
                title: formValue.title,
                comment: formValue.comment,
                rating: formValue.rating,
              }, user.id, review.productId).subscribe(() => {
                this.getReviewPageForUser();
              });
            }
          });
        }
      });
  }
}
