import { ErrorStateMatcher } from "@angular/material/core";
import { AbstractControl, FormControl, FormGroupDirective, NgForm } from "@angular/forms";

/**
 * Custom ErrorStateMatcher which returns true (error exists)
 * when the parent form group is invalid and passwordConfirmMatch returns true
 * used for <mat-error> to display proper error on invalid input
 */
export class PasswordConfirmationMatcher implements ErrorStateMatcher {

  constructor(
    private passwordField: string,
    private confirmPasswordField: string,
  ) {
  }

  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isTouchedOrSubmitted = !!((form && form.submitted) || (control && control.touched));
    const invalidControl = !!(control && control.invalid);
    const invalidConfirmPassword = !!(control && control.parent && control.parent.invalid
      && !confirmPasswordMatch(control.parent, this.passwordField, this.confirmPasswordField));

    return (isTouchedOrSubmitted && (invalidControl || invalidConfirmPassword));
  }
}

export const confirmPasswordMatch = (control: AbstractControl, passwordField: string, confirmPasswordField: string) => {
  const password = control.get(passwordField);
  const confirmPassword = control.get(confirmPasswordField);
  return password && confirmPassword && password.value === confirmPassword.value;
};
