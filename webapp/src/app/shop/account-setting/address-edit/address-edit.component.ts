import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { Address } from "../model/address.model";
import { stateName, stateValue } from "../../../core/constant/state.constant";

@Component({
  selector: 'app-address-edit',
  templateUrl: './address-edit.component.html',
  styleUrls: ['./address-edit.component.scss']
})
export class AddressEditComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<AddressEditComponent>,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: { address: Address },
  ) { }

  ngOnInit(): void {
  }

  saveEdit() {
    this.dialogRef.close({
      id: this.data.address.id,
      userId: this.data.address.userId,
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
    addressLine1: [this.data.address.addressLine1, { validators: [Validators.required] }],
    addressLine2: [this.data.address.addressLine2],
    city: [this.data.address.city, { validators: [Validators.required] }],
    state: [this.data.address.state, { validators: [Validators.required] }],
    postalCode: [this.data.address.postalCode, { validators: [Validators.required] }],
    primaryAddress: [this.data.address.primaryAddress, { validators: [Validators.required] }],
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
