import { createStore, select, withProps } from "@ngneat/elf";
import { localStorageStrategy, persistState } from "@ngneat/elf-persist-state";
import { Injectable } from "@angular/core";
import { User } from "../../model/user.model";

export const initialUser = (): User => ({
  id: "",
  email: "",
  firstName: "",
  lastName: "",
});

export interface AuthenticationProps {
  user: User,
  syncCart: boolean,
}

export const initialAuthenticationProps = (): AuthenticationProps => ({
  user: initialUser(),
  syncCart: false,
});

export const authStore = createStore(
  { name: "authentication" },
  withProps<AuthenticationProps>(initialAuthenticationProps()));

@Injectable({
  providedIn: "root",
})
export class AuthenticationRepository {
  user$ = authStore.pipe(select((state) => state.user));
  syncCart$ = authStore.pipe(select((state) => state.syncCart));

  updateSyncCart(syncCart: boolean) {
    authStore.update(state => ({ ...state, syncCart }));
  }

  updateUser(user: AuthenticationProps["user"]) {
    authStore.update(state => ({ ...state, user }));
  }
}

export const authPersist = persistState(authStore, {
  key: "auth",
  storage: localStorageStrategy,
});
