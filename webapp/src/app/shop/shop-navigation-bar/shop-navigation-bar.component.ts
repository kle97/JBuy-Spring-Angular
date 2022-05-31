import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-shop-navigation-bar',
  templateUrl: './shop-navigation-bar.component.html',
  styleUrls: ['./shop-navigation-bar.component.scss']
})
export class ShopNavigationBarComponent implements OnInit {

  title: string = "JBuy";

  constructor() { }

  ngOnInit(): void {
  }

}
