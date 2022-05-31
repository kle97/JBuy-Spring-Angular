import { User } from "../../../../core/model/user.model";
import { createStore, select, withProps } from "@ngneat/elf";
import { Injectable } from "@angular/core";
import { initialUser } from "../../../../core/repository/authentication/authentication.repository";

export const userStore = createStore({ name: "user" }, withProps<User>(initialUser()));

@Injectable({
  providedIn: "root",
})
export class UserRepository {
  user$ = userStore.pipe(select((state) => state));

  updateUser(user: User) {
    userStore.update(() => ({ ...user }));
  }
}
