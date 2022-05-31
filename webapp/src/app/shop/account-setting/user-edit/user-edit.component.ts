import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { User } from "../../../core/model/user.model";
import { stateName, stateValue } from "../../../core/constant/state.constant";

@Component({
  selector: "app-user-edit",
  templateUrl: "./user-edit.component.html",
  styleUrls: ["./user-edit.component.scss"],
})
export class UserEditComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<UserEditComponent>,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: { user: User },
  ) {
  }

  ngOnInit(): void {
  }

  saveEdit() {
    this.dialogRef.close({
      id: this.data.user.id,
      email: this.data.user.email,
      firstName: this.firstName.value,
      lastName: this.lastName.value,
    });
  }

  userForm = this.formBuilder.group({
    firstName: [this.data.user.firstName, { validators: [Validators.required] }],
    lastName: [this.data.user.lastName, { validators: [Validators.required] }],
  });

  get firstName() {
    return this.userForm.get("firstName") as FormControl;
  }

  get lastName() {
    return this.userForm.get("lastName") as FormControl;
  }
}
