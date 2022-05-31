import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { User } from "../../../core/model/user.model";
import { UserRepository } from "../repository/user/user.repository";
import { UserService } from "../repository/user/user.service";

@Component({
  selector: "app-user",
  templateUrl: "./user.component.html",
  styleUrls: ["./user.component.scss"],
})
export class UserComponent implements OnInit {

  user$: Observable<User> = this.userRepository.user$;

  constructor(
    private userRepository: UserRepository,
    private userService: UserService,
  ) {
  }

  editUser(user: User) {
    this.userService.editUser(user);
  }

  ngOnInit(): void {
    this.userService.getUser();
  }

}
