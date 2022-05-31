import { Injectable } from "@angular/core";
import { MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from "@angular/material/snack-bar";

@Injectable({
  providedIn: "root",
})
export class NotificationService {

  constructor(private snackBar: MatSnackBar) {
  }

  open(message: string,
       duration: number = 5000,
       horizontalPosition: MatSnackBarHorizontalPosition = "center",
       verticalPosition: MatSnackBarVerticalPosition = "top") {

    this.snackBar.open(message, "Close", {
      duration: duration,
      horizontalPosition: horizontalPosition,
      verticalPosition: verticalPosition,
      // panelClass: ["mat-toolbar", "mat-warn"],
    });
  }
}
