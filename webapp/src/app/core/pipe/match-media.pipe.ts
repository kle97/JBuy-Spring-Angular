import { Pipe, PipeTransform } from '@angular/core';
import { BreakpointObserver, Breakpoints } from "@angular/cdk/layout";
import { map, Observable } from "rxjs";

const breakPointArr = ["xs", "sm", "md", "lg", "xl", "ltMd", "ltLg", "ltXl", "gtXs", "gtSm", "gtMd"] as const;
export type BreakPoint = (typeof breakPointArr)[number];
const isBreakPoint = (value: any): value is BreakPoint => breakPointArr.includes(value);

const breakPoint = {
  xs: [Breakpoints.XSmall],
  sm: [Breakpoints.Small],
  md: [Breakpoints.Medium],
  lg: [Breakpoints.Large],
  xl: [Breakpoints.XLarge],
  ltMd: [Breakpoints.XSmall, Breakpoints.Small],
  ltLg: [Breakpoints.XSmall, Breakpoints.Small, Breakpoints.Medium],
  ltXl: [Breakpoints.XSmall, Breakpoints.Small, Breakpoints.Medium, Breakpoints.Large],
  gtXs: [Breakpoints.Small, Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge],
  gtSm: [Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge],
  gtMd: [Breakpoints.Large, Breakpoints.XLarge],
};

@Pipe({
  name: 'matchMedia'
})
export class MatchMediaPipe implements PipeTransform {

  constructor(
    private breakPointObserver: BreakpointObserver,
  ) {
  }

  transform(value: BreakPoint | string): Observable<boolean> {
    const size = isBreakPoint(value) ? breakPoint[value] : value;
    return this.breakPointObserver
      .observe(size)
      .pipe(
        map(breakPointState => breakPointState.matches),
      );
  }
}
