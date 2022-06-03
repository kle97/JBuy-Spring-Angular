import { createStore, select, withProps } from "@ngneat/elf";
import { localStorageStrategy, persistState } from "@ngneat/elf-persist-state";
import { Injectable } from "@angular/core";
import { User } from "../../model/user.model";
import { Observable, of, switchMap, take } from "rxjs";

export const initialUser = (): User => ({
  id: "",
  email: "",
  firstName: "",
  lastName: "",
  expiry: 0,
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
  expiry$ = authStore.pipe(select((state) => state.user.expiry));
  isLoggedIn$: Observable<boolean> = this.expiry$.pipe(
    take(1),
    switchMap(expiry => {
      const currentTimeStamp = Date.now();
      if (expiry > 0 && currentTimeStamp < expiry) {
        return of(true);
      } else {
        return of(false);
      }
    })
  );

  updateSyncCart(syncCart: boolean) {
    authStore.update(state => ({ ...state, syncCart }));
  }

  updateUser(user: AuthenticationProps["user"]) {
    authStore.update(state => ({
      ...state,
      user: {
        ...user,
        expiry: user.expiry > 0 ? Date.now() + Number(user.expiry) * 1000 : 0,
      }
    }));
  }

  updateUserWithoutExpiry(user: AuthenticationProps["user"]) {
    authStore.update(state => ({
      ...state,
      user: {
        ...user,
        expiry: state.user.expiry,
      }
    }));
  }
}

export const authPersist = persistState(authStore, {
  key: "auth",
  storage: localStorageStrategy,
});
