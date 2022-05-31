import { Component, OnDestroy, OnInit } from "@angular/core";
import { Title } from "@angular/platform-browser";
import { ChildrenOutletContexts, NavigationEnd, NavigationStart, Router } from "@angular/router";
import { Subject, takeUntil } from "rxjs";
import { ScrollUpService } from "./core/service/scroll-up.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
})
export class AppComponent implements OnInit, OnDestroy{

  private unsubscribe$ = new Subject<void>();
  title = "JBuy";

  constructor(
    private titleService: Title,
    private contexts: ChildrenOutletContexts,
    private router: Router,
    private scrollUpService: ScrollUpService,
  ) {
  }

  ngOnInit(): void {
    this.router.events.pipe(takeUntil(this.unsubscribe$)).subscribe(event => {
      if (event instanceof NavigationStart && event.navigationTrigger === "imperative") {
        this.scrollUpService.toTop("body");
      }
      if (event instanceof NavigationEnd) {
        let title = this.contexts.getContext("primary")?.route?.snapshot?.data?.["title"];
        if (!title) {
          title = this.contexts.getContext("primary")
            ?.children.getContext("primary")?.route?.snapshot.data?.["title"];
        }
        if (title) {
          this.titleService.setTitle(title);
        }
      }
    });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }
}
