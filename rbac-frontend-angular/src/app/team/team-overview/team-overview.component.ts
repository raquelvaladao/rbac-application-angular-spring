import { Component, OnInit } from '@angular/core';
import { PilotFirstLastRace } from 'src/app/autenticacao/model/PilotFirstLastRace';
import { PilotTotalVictories } from 'src/app/autenticacao/model/PilotTotalVictories';
import { PilotServiceService } from 'src/app/pilot/pilot-service.service';
import { TeamServiceService } from '../team-service.service';

export interface Tuple {
  info: string;
  quantity: number;
}

@Component({
  selector: 'app-team-overview',
  templateUrl: './team-overview.component.html',
  styleUrls: ['./team-overview.css']
})
export class TeamOverviewComponent implements OnInit {
  displayedColumns: string[] = ['demo-name', 'demo-symbol'];
  displayedColumnsVic: string[] = ['demo-name'];
  displayedColumnsPil: string[] = ['demo-name'];

  data: PilotFirstLastRace[] = [];
  datavic: PilotTotalVictories[] = [];
  datapil: PilotTotalVictories[] = [];
  

  constructor(
    private teamService: TeamServiceService,
  ) {}

  ngOnInit(): void {
    this.getData();
    this.getDataVictories();
    this.getDataPilots();
  }

  private getData() {
    this.teamService.getFirstLastYear().subscribe({
      next: (json: any) => {
        this.populateResponse(json.body);
        console.log(json.body)
      },
      error: () => {
        this.teamService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }

  private populateResponse(body: any) {
    this.data.push({ firstYear: body.firstYear, lastYear: body.lastYear });
  }

  private getDataVictories() {
    this.teamService.getVictories().subscribe({
      next: (json: any) => {
        this.populateResponseVictories(json.body);
        console.log(json.body)
      },
      error: () => {
        this.teamService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }

  private populateResponseVictories(body: any) {
    this.datavic.push({ quantity: body.quantity });
  }

  private getDataPilots() {
    this.teamService.getPilots().subscribe({
      next: (json: any) => {
        this.populateResponsePilots(json.body);
        console.log(json.body)

      },
      error: () => {
        this.teamService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }

  private populateResponsePilots(body: any) {
    this.datapil.push({ quantity: body.quantity });
  }

}
