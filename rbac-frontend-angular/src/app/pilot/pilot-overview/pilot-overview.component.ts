import { Component, OnInit } from '@angular/core';
import { PilotFirstLastRace } from 'src/app/autenticacao/model/PilotFirstLastRace';
import { Tuple } from 'src/app/autenticacao/model/Tuple';
import { PilotServiceService } from '../pilot-service.service';
import { PilotTotalVictories } from 'src/app/autenticacao/model/PilotTotalVictories';

@Component({
  selector: 'app-pilot-overview',
  templateUrl: './pilot-overview.component.html',
  styleUrls: ['./piloto-overview.css'],
})
export class PilotOverviewComponent implements OnInit {
  displayedColumns: string[] = ['demo-name', 'demo-symbol'];
  displayedColumnsVic: string[] = ['demo-name'];
  data: PilotFirstLastRace[] = [];
  datavic: PilotTotalVictories[] = [];
  

  constructor(
    private pilotService: PilotServiceService,
  ) {}

  ngOnInit(): void {
    this.getData();
    this.getDataVictories();
  }

  private getData() {
    this.pilotService.getFirstLastYear().subscribe({
      next: (json: any) => {
        this.populateResponse(json.body);
        console.log(json.body.firstYear)
      },
      error: () => {
        this.pilotService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }

  private populateResponse(body: any) {
    this.data.push({ firstYear: body.firstYear, lastYear: body.lastYear });
  }

  private getDataVictories() {
    this.pilotService.getVictories().subscribe({
      next: (json: any) => {
        this.populateResponseVictories(json.body);
      },
      error: () => {
        this.pilotService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }

  private populateResponseVictories(body: any) {
    this.datavic.push({ quantity: body.quantity });
  }
}
