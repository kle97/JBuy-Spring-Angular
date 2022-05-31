import { AfterContentInit, AfterViewInit, Component, ElementRef, HostListener, OnInit, ViewChild } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { CookieService } from "ngx-cookie";
import { environment } from "../../../environments/environment";

@Component({
  selector: "app-ui",
  templateUrl: "./ui.component.html",
  styleUrls: ["./ui.component.scss"],
})
export class UiComponent implements OnInit, AfterViewInit {

  specUrl: string = environment.specUrl;
  securityScheme: string = environment.securityScheme;
  email: string = environment.email;
  password: string = environment.password;

  @ViewChild("theDoc") rapidocEl!: ElementRef;

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
  ) {
  }

  httpOptions = {
    headers: new HttpHeaders({ "Content-Type": "application/json" }),
    withCredentials: true,
  };

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.rapidocEl.nativeElement.setAttribute("spec-url", this.specUrl);
    this.rapidocEl.nativeElement.setAttribute("goto-path", "auth");
    this.rapidocEl.nativeElement.setAttribute("route-prefix", "rapidoc#");
    this.rapidocEl.nativeElement.setAttribute("fetch-credentials", "include");
    this.http.get("/auth/csrf", this.httpOptions).subscribe();
  }

  @HostListener("before-try", ["$event"])
  onBeforeTry(event: CustomEvent) {
    const csrfTokenCookieName = "XSRF-TOKEN";
    const csrfTokenHeaderName = "X-XSRF-TOKEN";
    event.detail.request.headers.append("X-Requested-With", "XMLHttpRequest");

    if (event.detail.request.method === "POST"
      || event.detail.request.method === "PUT"
      || event.detail.request.method === "PATCH"
      || event.detail.request.method === "DELETE") {
      const csrfCookie = this.cookieService.get(csrfTokenCookieName);
      event.detail.request.headers.append(csrfTokenHeaderName, csrfCookie);
    }
  }

  onBtn1() {
    this.rapidocEl.nativeElement.setAttribute("spec-url", this.specUrl + "/jbuy-user");
  }

  onBtn2() {
    this.rapidocEl.nativeElement.setAttribute("spec-url", this.specUrl + "/jbuy-admin");
  }

  onBtn3() {
    this.rapidocEl.nativeElement.setAttribute("spec-url", this.specUrl);
  }

  onBtn4() {
    this.rapidocEl.nativeElement.setAttribute("use-path-in-nav-bar", "true");
  }

  onBtn5() {
    this.rapidocEl.nativeElement.setAttribute("use-path-in-nav-bar", "false");
  }

  onBtn6() {
    this.rapidocEl.nativeElement.setHttpUserNameAndPassword(this.securityScheme, this.email, this.password);
  }

  onBtn7() {
    this.rapidocEl.nativeElement.removeAllSecurityKeys();
  }

}
