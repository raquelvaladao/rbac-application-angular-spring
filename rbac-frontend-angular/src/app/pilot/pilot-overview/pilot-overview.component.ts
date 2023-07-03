import { Component, OnInit } from '@angular/core';
import { PilotService } from '../pilot.service';
import { Tuple } from 'src/app/autenticacao/model/Tuple';

@Component({
  selector: 'app-pilot-overview',
  templateUrl: './pilot-overview.component.html',
  styleUrls: ['./piloto-overview.css'],
})
export class PilotOverviewComponent implements OnInit {
  displayedColumns: string[] = ['demo-name', 'demo-symbol'];
  displayedColumnsYear: string[] = ['demo-name', 'demo-symbol', 'demo-symbol-2'];

  data: Tuple[] = [];
  ELEMENT_DATA_YEAR: Tuple[] = [
    {info: 'Primeiro e último ano em que há dados do piloto na base', quantity: 44},
  ];

  constructor(private pilotService: PilotService) { }

  ngOnInit(): void {
    this.getData();
  }

  private getData() {
    this.pilotService.getOverview().subscribe({
      next: (json: any) => {
        console.log(json)
        this.populateResponse(json.body);
      },
      error: () => {
        this.pilotService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }

  private populateResponse(json: any) {
    this.data.push({ info: 'Vitórias', quantity: json.quantity });
  }

}
