import { Component, OnInit } from "@angular/core";
import { MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormControl, Validators } from "@angular/forms";

@Component({
  selector: "app-review-create",
  templateUrl: "./review-create.component.html",
  styleUrls: ["./review-create.component.scss"],
})
export class ReviewCreateComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<ReviewCreateComponent>,
    private formBuilder: FormBuilder,
  ) {
  }

  ngOnInit(): void {
  }

  create() {
    this.dialogRef.close({
      title: this.title.value,
      comment: this.comment.value,
      rating: this.rating.value,
    });
  }

  onSelectedStar(star: number) {
    this.rating.setValue(star)
  }

  reviewForm = this.formBuilder.group({
    title: ["", { validators: [Validators.required] }],
    comment: ["", { validators: [Validators.required] }],
    rating: [0, { validators: [Validators.required] }],
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
