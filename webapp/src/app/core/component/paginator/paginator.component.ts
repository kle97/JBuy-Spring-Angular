import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { PageEvent } from "@angular/material/paginator";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { ThemePalette } from "@angular/material/core";
import { ScrollUpService } from "../../service/scroll-up.service";

@Component({
  selector: "app-paginator",
  templateUrl: "./paginator.component.html",
  styleUrls: ["./paginator.component.scss"],
})
export class PaginatorComponent implements OnInit {

  _pageSize: number = 20;
  _length: number = 1161;
  _pageIndex: number = 0;

  get pageSize() {
    return this._pageSize;
  }

  @Input() set pageSize(newValue) {
    this._pageSize = newValue;
    this.updatePageInfo();
  }

  get length() {
    return this._length;
  }

  @Input() set length(newValue) {
    this._length = newValue;
    this.updatePageInfo();
  }

  get pageIndex() {
    return this._pageIndex;
  }

  @Input() set pageIndex(newValue) {
    this._pageIndex = newValue;
    this.updatePageInfo();
  }

  math: Math = Math;
  @Input() showPageInfo: boolean = true;
  @Input() pageInfoLabel: string = "items";
  @Input() showPageOption: boolean = true;
  @Input() pageOptionLabel: string = "Items";
  @Input() showGotoPage: boolean = true;
  @Input() previousLabel: string = "Prev";
  @Input() nextLabel: string = "Next";
  @Input() showPaginator: boolean = true;
  @Input() align: "center" | "start" | "end" = "center"
  @Input() color: ThemePalette;
  @Input() pageSizeOptions: number[] = [5, 10, 20, 25, 50, 100];
  @Output() page = new EventEmitter<PageEvent>();
  maxPageNumber: number = Math.ceil(this.length / this.pageSize);

  buttonNumbers() {
    return [...Array(this.maxPageNumber).keys()].map(i => i + 1);
  }

  goToPage(page: number) {
    if (page === this._pageIndex || page + 1 > this.maxPageNumber || page + 1 < 1) {
      return;
    }
    this._pageIndex = page;
    this.updatePageInfo();

    this.onPageEvent();
  }

  onPageEvent() {
    const pageEvent: PageEvent = {
      length: this._length,
      pageIndex: this._pageIndex,
      pageSize: this._pageSize,
    };
    this.page.emit(pageEvent);
    this.scrollUpService.scrollIntoView("body");
  }

  pageSizeChange() {
    this._pageIndex = 0;
    this.updatePageInfo();
    this.onPageEvent();
  }

  updatePageInfo() {
    this.pageNumber.setValue(this.pageIndex + 1);
    this.maxPageNumber = Math.ceil(this.length / this.pageSize);
  }

  isHiddenButton(number: number) {
    const current = this.pageIndex + 1;
    if (current <= 3 && number <= 6) {
      return false;
    } else if (current >= this.maxPageNumber - 2 && number >= this.maxPageNumber - 5) {
      return false;
    } else {
      return !(number === 1
        || number === this.maxPageNumber
        || number === current + 2
        || number === current + 1
        || number === current
        || number === current - 1
        || number === current - 2);
    }
  }

  isEllipsisButton(number: number) {
    const current = this.pageIndex + 1;
    if (current <= 4 && number <= 5) {
      return false;
    }
    if (current <= 3 && number === 6 && number !== this.maxPageNumber && number !== this.maxPageNumber - 1) {
      return true;
    } else if (current >= this.maxPageNumber - 3 && number >= this.maxPageNumber - 4) {
      return false;
    } else if (current >= this.maxPageNumber - 2 && number === this.maxPageNumber - 5 && number !== 1 && number !== 2) {
      return true;
    } else {
      return (number === this.pageIndex + 3 || number === this.pageIndex - 1)
        && !(number === 1 || number === this.maxPageNumber);
    }
  }

  pageNumberForm = this.formBuilder.group({
    pageNumber: [1, { validators: [Validators.required] }],
  });

  get pageNumber() {
    return this.pageNumberForm.get("pageNumber") as FormControl;
  }

  constructor(
    private formBuilder: FormBuilder,
    private scrollUpService: ScrollUpService,
  ) {
  }

  ngOnInit(): void {
  }

}
