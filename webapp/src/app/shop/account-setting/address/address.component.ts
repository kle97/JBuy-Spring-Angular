import { Component, OnInit, TrackByFunction } from "@angular/core";
import { Address } from "../model/address.model";
import { AddressService } from "../repository/address/address.service";
import { Observable } from "rxjs";
import { AddressRepository } from "../repository/address/address.repository";

@Component({
  selector: "app-addresses",
  templateUrl: "./address.component.html",
  styleUrls: ["./address.component.scss"],
})
export class AddressComponent implements OnInit {

  addresses$: Observable<Array<Address>> = this.addressRepository.addresses$;

  constructor(
    private addressRepository: AddressRepository,
    private addressService: AddressService,
  ) {
  }

  ngOnInit(): void {
    this.addressService.getPage();
  }

  createAddress() {
    this.addressService.createAddress();
  }

  editAddress(address: Address) {
    this.addressService.editAddress(address);
  }

  removeAddress(address: Address) {
    this.addressService.removeAddress(address);
  }

  trackAddress: TrackByFunction<Address> = (index: number, address: Address): string => {
    return address.id;
  };
}
