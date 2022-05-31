import { AfterViewInit, Component, Input, OnInit } from "@angular/core";

@Component({
  selector: 'app-loading-svg',
  templateUrl: './loading-svg.component.html',
  styleUrls: ['./loading-svg.component.scss']
})
export class LoadingSvgComponent implements OnInit, AfterViewInit {

  @Input() color: string = "black";
  @Input() width: number = 100;
  @Input() height: number = 100;

  constructor() {
  }

  ngOnInit(): void {

  }

  ngAfterViewInit(): void {

  }

}
