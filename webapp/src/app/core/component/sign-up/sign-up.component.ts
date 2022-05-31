import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { SignUpService } from "../../service/sign-up.service";
import { confirmPasswordMatch, PasswordConfirmationMatcher } from "../../matcher/password-confirmation.matcher";
import { MatSlideToggleChange } from "@angular/material/slide-toggle";
import { ExistingEmailValidator } from "../../validator/existing-email.validator";

@Component({
  selector: "app-sign-up",
  templateUrl: "./sign-up.component.html",
  styleUrls: ["./sign-up.component.scss"],
})
export class SignUpComponent implements OnInit {

  hidePassword = true;
  errorMessage$ = this.signUpService.errorMessage$;
  matcher = new PasswordConfirmationMatcher("password", "confirmPassword");

  constructor(
    private formBuilder: FormBuilder,
    private signUpService: SignUpService,
    private existingEmailValidator: ExistingEmailValidator,
  ) {
  }

  signUpForm = this.formBuilder.group({
    email: ["", {
      validators: [Validators.required, Validators.email, Validators.maxLength(120)],
      asyncValidators: [this.existingEmailValidator],
      updateOn: "blur",
    }],
    password: ["", {
      validators: [
        Validators.required,
        Validators.pattern("(?=.*[a-zA-Z])((?=.*[0-9])|(?=.*[!@#$%^&*{}[\\\]|\\\\/?<>,.;:'\"-_+=()`~])).{8,}"),
      ],
    }],
    confirmPassword: ["", { validators: [Validators.required] }],
    firstName: ["", { validators: [Validators.required, Validators.maxLength(40)] }],
    lastName: ["", { validators: [Validators.required, Validators.maxLength(40)] }],
  }, { validators: this.passwordMatchValidator });

  passwordMatchValidator(formGroup: FormGroup) {
    return confirmPasswordMatch(formGroup, "password", "confirmPassword")
      ? null : { "mismatch": true };
  }

  get email() {
    return this.signUpForm.get("email") as FormControl;
  }

  get password() {
    return this.signUpForm.get("password") as FormControl;
  }

  get confirmPassword() {
    return this.signUpForm.get("confirmPassword") as FormControl;
  }

  get firstName() {
    return this.signUpForm.get("firstName") as FormControl;
  }

  get lastName() {
    return this.signUpForm.get("lastName") as FormControl;
  }

  onSubmit() {
    this.signUpService.signUp(this.email.value, this.password.value, this.firstName.value, this.lastName.value);
  }

  onShowPasswordChange($event: MatSlideToggleChange) {
    this.hidePassword = !$event.checked;
  }

  ngOnInit(): void {
  }

}
