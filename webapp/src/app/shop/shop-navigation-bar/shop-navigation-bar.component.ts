import { Component, OnInit, ViewChild } from "@angular/core";
import { MatSidenav } from "@angular/material/sidenav";
import { UnsubscribeComponent } from "../../core/component/unsubscribe/unsubscribe.component";
import { BreakpointObserver, Breakpoints } from "@angular/cdk/layout";
import { takeUntil } from "rxjs";

@Component({
  selector: 'app-shop-navigation-bar',
  templateUrl: './shop-navigation-bar.component.html',
  styleUrls: ['./shop-navigation-bar.component.scss']
})
export class ShopNavigationBarComponent extends UnsubscribeComponent implements OnInit {

  title: string = "JBuy";
  @ViewChild("sidenav") sidenav!: MatSidenav;

  constructor(
    private breakpointObserver: BreakpointObserver,
  ) {
    super();
  }

  ngOnInit(): void {
    this.breakpointObserver
      .observe([Breakpoints.Small, Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge])
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(() => {
        if (this.sidenav) {
          this.sidenav.close();
        }
      })
  }

  toggle() {
    this.sidenav.toggle();
  }

}
