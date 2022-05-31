import { Component, OnInit } from '@angular/core';
import { PasswordChangeRequest } from "../model/password-change-request.model";
import { UserService } from "../repository/user/user.service";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { confirmPasswordMatch, PasswordConfirmationMatcher } from "../../../core/matcher/password-confirmation.matcher";
import { MatSlideToggleChange } from "@angular/material/slide-toggle";

@Component({
  selector: 'app-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.scss']
})
export class PasswordComponent implements OnInit {

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
  ) {
  }

  hidePassword = true;
  matcher = new PasswordConfirmationMatcher("newPassword", "confirmNewPassword");

  passwordForm = this.formBuilder.group({
    currentPassword: ["", { validators: [Validators.required] }],
    newPassword: ["", {
      validators: [
        Validators.required,
        Validators.pattern("(?=.*[a-zA-Z])((?=.*[0-9])|(?=.*[!@#$%^&*{}[\\\]|\\\\/?<>,.;:'\"-_+=()`~])).{8,}"),
      ],
    }],
    confirmNewPassword: ["", { validators: [Validators.required] }],
  }, { validators: this.newPasswordMatchValidator });

  newPasswordMatchValidator(formGroup: FormGroup) {
    return confirmPasswordMatch(formGroup, "newPassword", "confirmNewPassword")
      ? null : { "mismatch": true };
  }

  get currentPassword() {
    return this.passwordForm.get("currentPassword") as FormControl;
  }

  get newPassword() {
    return this.passwordForm.get("newPassword") as FormControl;
  }

  get confirmNewPassword() {
    return this.passwordForm.get("confirmNewPassword") as FormControl;
  }

  changePassword() {
    this.userService.updatePassword({
      currentPassword: this.currentPassword.value,
      newPassword: this.newPassword.value,
    })
  }

  onShowPasswordChange($event: MatSlideToggleChange) {
    this.hidePassword = !$event.checked;
  }

  ngOnInit(): void {
  }

}
