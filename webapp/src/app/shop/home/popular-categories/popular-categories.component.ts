import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-popular-categories',
  templateUrl: './popular-categories.component.html',
  styleUrls: ['./popular-categories.component.scss']
})
export class PopularCategoriesComponent implements OnInit {

  hover: number = 0;

  constructor() { }

  ngOnInit(): void {
  }

}
