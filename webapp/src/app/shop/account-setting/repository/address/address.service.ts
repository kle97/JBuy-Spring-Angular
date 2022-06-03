import { Injectable } from "@angular/core";
import { AbstractGenericCrudService } from "../../../../core/service/generic-crud.service";
import { Address } from "../../model/address.model";
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { Observable, take, throwError } from "rxjs";
import { ErrorNotificationService } from "../../../../core/service/error-notification.service";
import { AddressEditComponent } from "../../address-edit/address-edit.component";
import { MatDialog } from "@angular/material/dialog";
import { NotificationService } from "../../../../core/service/notification.service";
import { AuthenticationRepository } from "../../../../core/repository/authentication/authentication.repository";
import { AddressRepository } from "./address.repository";
import { ConfirmDialogComponent } from "../../../../core/component/confirm-dialog/confirm-dialog.component";
import { AddressCreateComponent } from "../../address-create/address-create.component";

@Injectable({
  providedIn: "root",
})
export class AddressService extends AbstractGenericCrudService<Address, string> {

  constructor(
    protected override http: HttpClient,
    private authenticationRepository: AuthenticationRepository,
    private addressRepository: AddressRepository,
    private errorNotificationService: ErrorNotificationService,
    private dialog: MatDialog,
    private notificationService: NotificationService,
  ) {
    super(http, "/addresses", {
      readPageUrl: "/:id/page",
      readAll: false,
    });
  }

  protected override handleError(errorResponse: HttpErrorResponse): Observable<never> {
    this.errorNotificationService.open(errorResponse);
    return throwError(() => errorResponse);
  }

  getPage() {
    this.authenticationRepository.user$.pipe(
      take(1),
    ).subscribe(user => {
      if (user.expiry > 0 && Date.now() < user.expiry) {
        this.readPage(new HttpParams(), user.id).subscribe(addressPage => {
          this.addressRepository.setAddresses(addressPage.content);
        });
      }
    });
  }

  createAddress() {
    this.authenticationRepository.user$.pipe(
      take(1),
    ).subscribe(user => {
      if (user.expiry > 0 && Date.now() < user.expiry) {
        const dialogRef = this.dialog.open(AddressCreateComponent, { width: "70rem", disableClose: true });
        dialogRef.afterClosed().subscribe((formValue: Address) => {
          if (formValue) {
            formValue.userId = user.id;
            this.create(formValue).subscribe(() => {
              this.notificationService.open("New Address Created!", 2000);
              this.getPage();
            });
          }
        });
      }
    });
  }

  editAddress(address: Address) {
    const dialogRef = this.dialog.open(AddressEditComponent, {
      data: { address: address },
      width: "70rem",
      disableClose: true,
    });
    dialogRef.afterClosed().subscribe((formValue: Address) => {
      if (formValue) {
        this.update(formValue, address.id).subscribe(() => {
          this.notificationService.open("Changes Saved!", 2000);
          this.getPage();
        });
      }
    });
  }

  removeAddress(address: Address) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        width: "50rem",
        data: {
          message: "Do you want to remove address: " +
            address.addressLine1 + ", " +
            (address.addressLine2 ? address.addressLine1 + ", " : "") +
            address.city + ", " +
            address.state + " " +
            address.postalCode + "?",
        },
      },
    );

    dialogRef.afterClosed().subscribe(confirm => {
      if (confirm === true) {
        this.delete(address.id).subscribe(() => {
          this.notificationService.open("Address Deleted!", 2000);
          this.getPage();
        });
      }
    });
  }
}
