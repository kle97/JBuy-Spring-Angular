import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { Review } from "../model/review.model";

@Component({
  selector: "app-review-edit",
  templateUrl: "./review-edit.component.html",
  styleUrls: ["./review-edit.component.scss"],
})
export class ReviewEditComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<ReviewEditComponent>,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: { review: Review },
  ) {
  }

  ngOnInit(): void {
  }

  saveEdit() {
    this.dialogRef.close({
      title: this.title.value,
      comment: this.comment.value,
      rating: this.rating.value,
    });
  }

  onSelectedStar(star: number) {
    this.rating.setValue(star);
    this.reviewForm.markAsDirty();
  }

  reviewForm = this.formBuilder.group({
    title: [this.data.review.title, { validators: [Validators.required] }],
    comment: [this.data.review.comment, { validators: [Validators.required] }],
    rating: [this.data.review.rating, { validators: [Validators.required] }],
  });

  get title() {
    return this.reviewForm.get("title") as FormControl;
  }

  get comment() {
    return this.reviewForm.get("comment") as FormControl;
  }

  get rating() {
    return this.reviewForm.get("rating") as FormControl;
  }
}
