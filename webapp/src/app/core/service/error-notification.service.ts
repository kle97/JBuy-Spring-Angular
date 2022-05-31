import { HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from "@angular/material/snack-bar";
import { NotificationService } from "./notification.service";

@Injectable({
  providedIn: "root",
})
export class ErrorNotificationService {

  constructor(
    private notificationService: NotificationService,
  ) {
  }

  open(errorResponse: any,
       duration: number = 5000,
       horizontalPosition: MatSnackBarHorizontalPosition = "center",
       verticalPosition: MatSnackBarVerticalPosition = "top") {

    const error = errorResponse.error || null;

    const message = errorResponse instanceof HttpErrorResponse && error != null
      ? error.status + " " + error.error + ": " + error.message
      : errorResponse.message ? errorResponse.message : errorResponse;

    this.notificationService.open(
      message,
      duration,
      horizontalPosition,
      verticalPosition,
    );
  }
}
