import { Component, OnInit } from '@angular/core';

export interface Tuple {
  info: string;
  quantity: number;
}

@Component({
  selector: 'app-team-overview',
  templateUrl: './team-overview.component.html',
  styles: [
  ]
})
export class TeamOverviewComponent implements OnInit {
  displayedColumns: string[] = ['demo-name', 'demo-symbol'];
  displayedColumnsYear: string[] = ['demo-name', 'demo-symbol', 'demo-symbol-2'];
  ELEMENT_DATA: Tuple[] = [
    {info: 'Vitórias da escuderia', quantity: 44},
    {info: 'Pilotos diferentes que já correram pela escuderia', quantity: 44},
  ];
  ELEMENT_DATA_YEAR: Tuple[] = [
    {info: 'Primeiro e último ano em que há dados da escuderia na base', quantity: 56},
  ];
  constructor() { }

  ngOnInit(): void {
  }

}
