import { ChangeDetectionStrategy, Component, OnInit } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { AuthenticationService } from "../../repository/authentication/authentication.service";
import { ActivatedRoute } from "@angular/router";
import { takeUntil } from "rxjs";
import { UnsubscribeComponent } from "../unsubscribe/unsubscribe.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginComponent extends UnsubscribeComponent implements OnInit {

  mouseIn = false;
  hidePassword = true;
  hideVisibilityIcon = true;
  errorMessage$ = this.authenticationService.errorMessage$;
  redirectUrl: string | null = "";

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private activatedRoute: ActivatedRoute,
  ) {
    super();
  }

  loginForm = this.formBuilder.group({
    email: ["", { validators: [Validators.required, Validators.email, Validators.maxLength(120)] }],
    password: ["", { validators: [Validators.required] }],
  });

  get email() {
    return this.loginForm.get("email") as FormControl;
  }

  get password() {
    return this.loginForm.get("password") as FormControl;
  }

  onSubmit() {
    if (this.redirectUrl) {
      this.authenticationService.login(this.email.value, this.password.value, this.redirectUrl);
    } else {
      this.authenticationService.login(this.email.value, this.password.value);
    }
  }

  ngOnInit(): void {
    this.activatedRoute.queryParamMap.pipe(takeUntil(this.unsubscribe$)).subscribe(params => {
      this.redirectUrl = params.get("redirect");
    });
  }

}
