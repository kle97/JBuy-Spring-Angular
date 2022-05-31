import { AbstractControl, AsyncValidator, ValidationErrors } from "@angular/forms";
import { Injectable } from "@angular/core";
import { catchError, EMPTY, map, Observable } from "rxjs";
import { SignUpService } from "../service/sign-up.service";

@Injectable({
  providedIn: "root",
})
export class ExistingEmailValidator implements AsyncValidator {

  constructor(
    private signUpService: SignUpService,
  ) {
  }

  validate(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.signUpService.isEmailAlreadyRegistered(control.value).pipe(
      map(isRegistered => isRegistered ? ({ isRegistered: true }) : null),
      catchError(() => EMPTY),
    );
  }
}
