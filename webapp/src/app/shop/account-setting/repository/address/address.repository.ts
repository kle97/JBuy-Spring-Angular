import { createStore } from "@ngneat/elf";
import {
  addEntitiesFifo,
  deleteEntities,
  hasEntity,
  selectAllEntities,
  setEntities,
  withEntities,
} from "@ngneat/elf-entities";
import { Injectable } from "@angular/core";
import { Address } from "../../model/address.model";

export const addressStore = createStore({ name: "address" }, withEntities<Address>());

@Injectable({
  providedIn: "root",
})
export class AddressRepository {
  addresses$ = addressStore.pipe(selectAllEntities());

  setAddresses(addresses: Array<Address>) {
    addressStore.update(setEntities(addresses));
  }
}
