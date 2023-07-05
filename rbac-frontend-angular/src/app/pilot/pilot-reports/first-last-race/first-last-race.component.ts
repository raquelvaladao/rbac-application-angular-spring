import { Component, OnInit } from '@angular/core';
import { PilotServiceService } from '../../pilot-service.service';
import { PilotFirstLastRace } from 'src/app/autenticacao/model/PilotFirstLastRace';

@Component({
  selector: 'app-first-last-race',
  templateUrl: './first-last-race.component.html',
  styleUrls: ['./pilot-repo.css']
})
export class FirstLastRaceComponent implements OnInit {
  displayedColumns: string[] = ['demo-name', 'demo-symbol'];
  data: PilotFirstLastRace[] = [];

  constructor(
    private pilotService: PilotServiceService,
  ) {}

  ngOnInit(): void {
    this.getData();
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
}
