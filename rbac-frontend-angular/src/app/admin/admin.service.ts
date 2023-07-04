import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { PositionReportTuple } from '../autenticacao/model/PositionReportTuple';
import { AdminOverview } from '../autenticacao/model/AdminOverview';
import { CityReport } from '../autenticacao/model/CityReport';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  constructor(
    private http: HttpClient,
    private _snackBar: MatSnackBar,
    ) { }

  ADMIN_URL = 'http://localhost:8080/admin'
  NEW_PILOT_ENDPOINT = '/pilot'
  OVERVIEW_ENDPOINT = '/overview'
  REPORT_POSITION_ENDPOINT = '/report/position'
  REPORT_CITIES_ENDPOINT = '/report/cities'
  

  getOverview(): Observable<AdminOverview> {
    return this.http.get<AdminOverview>(this.ADMIN_URL.concat(this.OVERVIEW_ENDPOINT));
  }

  getPositionReport(): Observable<PositionReportTuple[]> {
    return this.http.get<PositionReportTuple[]>(this.ADMIN_URL.concat(this.REPORT_POSITION_ENDPOINT));
  }

  getCitiesReport(searchTerm: string): Observable<CityReport[]> {
    return this.http.get<CityReport[]>(this.ADMIN_URL.concat(this.REPORT_CITIES_ENDPOINT).concat('?cityName=').concat(searchTerm));
  }
  
  registerNewPilot(request: any) {
    this.http.post(this.ADMIN_URL.concat(this.NEW_PILOT_ENDPOINT), request).subscribe({
      next: () => {
        this.openSnackBar('Piloto criado com sucesso', 'green-snackbar');
      },
      error: (error) => {
        if(error['status'] != '201'){
          this.openSnackBar(error.error.message, 'red-snackbar');
        }
      },
    });
  }

  openSnackBar(message: string, color: string) {
    this._snackBar.open(message, 'Fechar', {
      duration: 5000,
      panelClass: [color],
      verticalPosition: 'top',
    });
  }
}
