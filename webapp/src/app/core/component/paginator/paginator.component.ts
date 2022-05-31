import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from "@angular/core";
import { ThemePalette } from "@angular/material/core";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { FormBuilder, FormControl, Validators } from "@angular/forms";

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.scss']
})
export class PaginatorComponent implements OnInit {

  private _pageSize: number = 20;
  private _length: number = 0;
  private _pageIndex: number = 0;

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

  @Input() pageSizeOptions: number[] = [5, 10, 20, 25, 50, 100];
  @Input() showFirstLastButtons: boolean = false;
  @Input() hidePageSize: boolean = false;
  @Input() disabled: boolean = false;
  @Input() color: ThemePalette;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @Output() page = new EventEmitter<PageEvent>();
  maxPageNumber: number = 1;

  goToPage() {
    if (this.pageNumber.value - 1 === this.paginator.pageIndex) {
      return;
    }
    this.paginator.pageIndex = this.pageNumber.value - 1;
    const event: PageEvent = {
      length: this.paginator.length,
      pageIndex: this.paginator.pageIndex,
      pageSize: this.paginator.pageSize,
    };
    this.paginator.page.next(event);
    this.onPageEvent(event);
  }

  onPageEvent(pageEvent: PageEvent) {
    this.pageNumber.setValue(pageEvent.pageIndex + 1);
    this.maxPageNumber = Math.ceil(this.length / this.pageSize);
    this.page.emit(pageEvent);
  }

  updatePageInfo() {
    this.pageNumber.setValue(this.pageIndex + 1);
    this.maxPageNumber = Math.ceil(this.length / this.pageSize);
  }

  pageNumberForm = this.formBuilder.group({
    pageNumber: [1, { validators: [Validators.required] }],
  });

  get pageNumber() {
    return this.pageNumberForm.get("pageNumber") as FormControl;
  }

  constructor(
    private formBuilder: FormBuilder,
  ) {
  }

  ngOnInit(): void {
  }

}
