import { Pipe, PipeTransform } from '@angular/core';
import { debounceTime, map, Observable } from "rxjs";
import { LoadingRepository } from "../repository/loading/loading.repository";

@Pipe({
  name: 'isLoading'
})
export class LoadingPipe implements PipeTransform {

  constructor(
    private loadingRepository: LoadingRepository,
  ) {
  }

  transform(value: boolean): Observable<boolean> {
    return this.loadingRepository.isLoading$.pipe(
      // debounceTime(0),
      map(isLoading => value === isLoading),
    );
  }
}
