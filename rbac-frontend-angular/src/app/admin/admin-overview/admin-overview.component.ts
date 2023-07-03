import { Component, OnInit } from '@angular/core';
import { AdminService } from '../admin.service';
import { Tuple } from 'src/app/autenticacao/model/Tuple';


@Component({
  selector: 'app-admin-overview',
  templateUrl: './admin-overview.component.html',
  styleUrls: ['./admin-overview.css'],
})
export class AdminOverviewComponent implements OnInit {
  displayedColumns: string[] = ['demo-name', 'demo-symbol'];
  data: Tuple[] = [];

  constructor(
    private adminService: AdminService,
  ) {}

  ngOnInit(): void {
    this.getData();
  }

  private getData() {
    this.adminService.getOverview().subscribe({
      next: (json: any) => {
        this.populateResponse(json.body);
        console.log(json)
      },
      error: () => {
        this.adminService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }

  private populateResponse(json: any) {
    this.data.push({ info: 'Pilotos', quantity: json.totalPilots });
    this.data.push({ info: 'Escuderias', quantity: json.totalTeams });
    this.data.push({ info: 'Corridas', quantity: json.totalRaces });
    this.data.push({ info: 'Temporadas', quantity: json.totalSeasons });
  }
}
