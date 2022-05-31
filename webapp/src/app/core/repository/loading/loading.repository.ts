import { createStore, select, withProps } from "@ngneat/elf";
import { Injectable } from "@angular/core";

export interface LoadingProps {
  isLoading: boolean,
  totalRequest: number,
}

export const initialLoadingProps = (): LoadingProps => ({
  isLoading: false,
  totalRequest: 0,
});

export const loadingStore = createStore({ name: "loading" }, withProps<LoadingProps>(initialLoadingProps()));

@Injectable({
  providedIn: "root",
})
export class LoadingRepository {
  isLoading$ = loadingStore.pipe(select((state) => state.isLoading));
  totalRequest$ = loadingStore.pipe(select((state) => state.totalRequest));

  loadingOn() {
    loadingStore.update((state) => ({
      ...state,
      isLoading: true,
    }));
  }

  loadingOff() {
    loadingStore.update((state) => ({
      ...state,
      isLoading: state.totalRequest > 0,
    }));
  }

  increaseRequest() {
    loadingStore.update(state => ({
      ...state,
      totalRequest: state.totalRequest + 1,
    }));
  }

  decreaseRequest() {
    loadingStore.update(state => ({
      ...state,
      totalRequest: state.totalRequest > 0 ? state.totalRequest - 1 : 0,
    }));
  }
}
