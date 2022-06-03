import { Injectable } from "@angular/core";
import { AbstractGenericCrudService } from "../../../../core/service/generic-crud.service";
import { User } from "../../../../core/model/user.model";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { UserRepository } from "./user.repository";
import { AuthenticationRepository } from "../../../../core/repository/authentication/authentication.repository";
import { catchError, Observable, take, throwError } from "rxjs";
import { ErrorNotificationService } from "../../../../core/service/error-notification.service";
import { MatDialog } from "@angular/material/dialog";
import { NotificationService } from "../../../../core/service/notification.service";
import { UserEditComponent } from "../../user-edit/user-edit.component";
import { PasswordChangeRequest } from "../../model/password-change-request.model";
import { AuthenticationService } from "../../../../core/repository/authentication/authentication.service";

@Injectable({
  providedIn: "root",
})
export class UserService extends AbstractGenericCrudService<User, string> {

  constructor(
    protected override http: HttpClient,
    private userRepository: UserRepository,
    private authenticationRepository: AuthenticationRepository,
    private authenticationService: AuthenticationService,
    private errorNotificationService: ErrorNotificationService,
    private dialog: MatDialog,
    private notificationService: NotificationService,
  ) {
    super(http, "/users", {
      readAll: false,
      readPage: false,
      create: false,
      delete: false,
    });
  }

  passwordChangeUrl: string = "/:id/change-password";

  protected override handleError(errorResponse: HttpErrorResponse): Observable<never> {
    this.errorNotificationService.open(errorResponse);
    return throwError(() => errorResponse);
  }

  getUser() {
    this.authenticationRepository.user$.pipe(take(1)).subscribe(user => {
      if (user.expiry > 0 && Date.now() < user.expiry) {
        this.readOne(user.id).subscribe(user => {
          this.userRepository.updateUser(user);
        })
      }
    });
  }

  updatePassword(passwordChangeRequest: PasswordChangeRequest) {
    this.authenticationRepository.user$.pipe(take(1)).subscribe(user => {
      if (user.expiry > 0 && Date.now() < user.expiry) {
        const url = this.getUrlWithId(this.entityUrl + this.passwordChangeUrl, user.id);
        this.http.post(url, passwordChangeRequest, this.httpOptions)
          .pipe(catchError(errorResponse => this.handleError(errorResponse)))
          .subscribe(() => {
            this.notificationService.open("Password Change Saved! Please login again.", 2000);
            this.authenticationService.logout("/login");
          });
      }
    });
  }

  editUser(user: User) {
    const dialogRef = this.dialog.open(UserEditComponent, {
      data: { user: user },
      width: "70rem",
      disableClose: true,
    });
    dialogRef.afterClosed().subscribe((formValue: User) => {
      if (formValue) {
        this.update(formValue, user.id).subscribe(user => {
          this.notificationService.open("Changes Saved!", 2000);
          this.userRepository.updateUser(user);
          this.authenticationRepository.updateUserWithoutExpiry(user);
        });
      }
    });
  }
}
