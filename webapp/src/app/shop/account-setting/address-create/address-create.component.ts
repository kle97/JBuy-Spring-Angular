import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { stateName, stateValue } from "../../../core/constant/state.constant";

@Component({
  selector: "app-address-create",
  templateUrl: "./address-create.component.html",
  styleUrls: ["./address-create.component.scss"],
})
export class AddressCreateComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<AddressCreateComponent>,
    private formBuilder: FormBuilder,
  ) {
  }

  ngOnInit(): void {
  }

  create() {
    this.dialogRef.close({
      addressLine1: this.addressLine1.value,
      addressLine2: this.addressLine2.value,
      city: this.city.value,
      state: this.state.value,
      postalCode: this.postalCode.value,
      primaryAddress: this.primaryAddress.value,
    });
  }

  stateNames: Array<string> = stateName;
  stateValues: Array<string> = stateValue;

  addressForm = this.formBuilder.group({
    addressLine1: ["", { validators: [Validators.required] }],
    addressLine2: [""],
    city: ["", { validators: [Validators.required] }],
    state: ["", { validators: [Validators.required] }],
    postalCode: ["", { validators: [Validators.required] }],
    primaryAddress: [false, { validators: [Validators.required] }],
  });

  get addressLine1() {
    return this.addressForm.get("addressLine1") as FormControl;
  }

  get addressLine2() {
    return this.addressForm.get("addressLine2") as FormControl;
  }

  get city() {
    return this.addressForm.get("city") as FormControl;
  }

  get state() {
    return this.addressForm.get("state") as FormControl;
  }

  get postalCode() {
    return this.addressForm.get("postalCode") as FormControl;
  }

  get primaryAddress() {
    return this.addressForm.get("primaryAddress") as FormControl;
  }

}
