import { Component, OnInit, ViewChild } from "@angular/core";
import { AuthenticationRepository } from "../../../core/repository/authentication/authentication.repository";
import { take } from "rxjs";

@Component({
  selector: "app-account-menu",
  templateUrl: "./account-menu.component.html",
  styleUrls: ["./account-menu.component.scss"],
})
export class AccountMenuComponent implements OnInit {

  isLoggedIn: boolean = false;
  username: string = "";

  constructor(
    private authenticationRepository: AuthenticationRepository,
  ) {
  }

  ngOnInit(): void {
    this.authenticationRepository.user$.pipe(
      take(1),
    ).subscribe(user => {
      this.isLoggedIn = !!user.id;
      this.username = user.firstName;
    });
  }
}
