import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { PilotFirstLastRace } from '../autenticacao/model/PilotFirstLastRace';

@Injectable({
  providedIn: 'root'
})
export class PilotServiceService {
  PILOT_URL = 'http://localhost:8080/pilot'
  OVERVIEW_ENDPOINT = '/overview'
  OVERVIEW_VICTORIES_ENDPOINT = '/overview/victories'
  OVERVIEW_YEAR_RACE_ENDPOINT = '/overview/races'

  REPORT_STATUS_ENDPOINT = '/report/status'
  REPORT_VICTORIES_ENDPOINT = '/report/victories'
  
  constructor(
    private http: HttpClient,
    private _snackBar: MatSnackBar,
    ) { }

  getFirstLastYear(): Observable<PilotFirstLastRace> {
    return this.http.get<PilotFirstLastRace>(this.PILOT_URL.concat(this.OVERVIEW_YEAR_RACE_ENDPOINT));
  }
  getVictories() {
    return this.http.get<PilotFirstLastRace>(this.PILOT_URL.concat(this.OVERVIEW_VICTORIES_ENDPOINT));
  }

  getStatus() {
    return this.http.get<PilotFirstLastRace>(this.PILOT_URL.concat(this.REPORT_STATUS_ENDPOINT));
  }

  getRollupVictories() {
    return this.http.get<any>(this.PILOT_URL.concat(this.REPORT_VICTORIES_ENDPOINT));
  }

  openSnackBar(message: string, color: string) {
    this._snackBar.open(message, 'Fechar', {
      duration: 5000,
      panelClass: [color],
      verticalPosition: 'top',
    });
  }
}
