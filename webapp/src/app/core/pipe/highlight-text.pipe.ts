import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'highlightText'
})
export class HighlightTextPipe implements PipeTransform {

  transform(value: string, textToHighLight: string): string {
    if (!textToHighLight) {
      return value;
    }
    const regex = new RegExp(textToHighLight, "gi");
    return value.replace(regex, "<b>$&</b>");
  }

}
