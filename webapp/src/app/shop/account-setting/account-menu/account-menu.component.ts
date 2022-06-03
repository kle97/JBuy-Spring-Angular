import { Component, EventEmitter, OnInit, Output } from "@angular/core";
import { AuthenticationRepository } from "../../../core/repository/authentication/authentication.repository";
import { Observable } from "rxjs";
import { User } from "../../../core/model/user.model";

@Component({
  selector: "app-account-menu",
  templateUrl: "./account-menu.component.html",
  styleUrls: ["./account-menu.component.scss"],
})
export class AccountMenuComponent implements OnInit {

  user$: Observable<User> = this.authenticationRepository.user$;
  @Output() onRouterLink = new EventEmitter<void>();

  constructor(
    private authenticationRepository: AuthenticationRepository,
  ) {
  }

  ngOnInit(): void {
  }

  isLoggedIn(expiry: number) {
    return expiry > 0 && Date.now() < expiry;
  }

  onLinkClick() {
    this.onRouterLink.emit();
  }
}
