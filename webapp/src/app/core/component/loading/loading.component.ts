import { Component, Input, OnInit } from "@angular/core";

@Component({
  selector: 'app-loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.scss']
})
export class LoadingComponent implements OnInit {

  @Input() overlay: boolean = false;
  @Input() color: "primary" | "accent" | "warn" = "primary";
  @Input() mode: "indeterminate" | "determinate" = "indeterminate";
  @Input() value: number = 0;

  constructor() { }

  ngOnInit(): void {
  }

}
