import { Component, OnInit } from '@angular/core';
import { PilotTotalVictories } from 'src/app/autenticacao/model/PilotTotalVictories';
import { PilotServiceService } from '../../pilot-service.service';

@Component({
  selector: 'app-victories',
  templateUrl: './victories.component.html',
  styleUrls: ['./pilot-repo.css']
})
export class VictoriesComponent implements OnInit {
  displayedColumns: string[] = ['demo-name'];
  data: PilotTotalVictories[] = [];

  constructor(
    private pilotService: PilotServiceService,
  ) {}

  ngOnInit(): void {
    this.getData();
  }

  private getData() {
    this.pilotService.getVictories().subscribe({
      next: (json: any) => {
        this.populateResponse(json.body);
      },
      error: () => {
        this.pilotService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }

  private populateResponse(body: any) {
    this.data.push({ quantity: body.quantity });
  }

}
