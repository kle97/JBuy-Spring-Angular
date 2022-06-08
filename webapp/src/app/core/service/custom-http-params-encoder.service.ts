import { HttpParameterCodec } from "@angular/common/http";
import { Injectable } from "@angular/core";

// custom http params encoder for proper encoding
// it will encode special symbols that were not encoded in Angular default encoder
// i.e "! $ \' ( ) * + , ; A 9 - . _ ~ ? /"
@Injectable({
  providedIn: "root",
})
export class CustomHttpParamsEncoderService implements HttpParameterCodec {
  encodeKey(key: string): string {
    return encodeURIComponent(key);
  }

  encodeValue(value: string): string {
    return encodeURIComponent(value);
  }

  decodeKey(key: string): string {
    return decodeURIComponent(key);
  }

  decodeValue(value: string): string {
    return decodeURIComponent(value);
  }
}
