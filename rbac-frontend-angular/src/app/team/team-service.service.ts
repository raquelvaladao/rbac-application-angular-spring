import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { PilotFirstLastRace } from '../autenticacao/model/PilotFirstLastRace';
import { PilotSearch } from '../autenticacao/model/PilotSearch';

@Injectable({
  providedIn: 'root'
})
export class TeamServiceService {
  TEAM_URL = 'http://localhost:8080/team'

  OVERVIEW_VICTORIES_ENDPOINT = '/overview/victories'
  OVERVIEW_YEAR_DATA_ENDPOINT = '/overview/years-data'
  OVERVIEW_PILOTS_ENDPOINT = '/overview/pilots'
  
  REPORT_PILOTS_ENDPOINT = '/report/pilots'
  REPORT_STATUS_ENDPOINT = '/report/status'
  SEARCH_PILOT_ENDPOINT = '/search/pilot'
  

  constructor(
    private http: HttpClient,
    private _snackBar: MatSnackBar,
    ) { }

  getFirstLastYear(): Observable<PilotFirstLastRace> {
    return this.http.get<PilotFirstLastRace>(this.TEAM_URL.concat(this.OVERVIEW_YEAR_DATA_ENDPOINT));
  }
  getVictories() {
    return this.http.get<PilotFirstLastRace>(this.TEAM_URL.concat(this.OVERVIEW_VICTORIES_ENDPOINT));
  }

  getPilots() {
    return this.http.get<PilotFirstLastRace>(this.TEAM_URL.concat(this.OVERVIEW_PILOTS_ENDPOINT));
  }

  getStatus() {
    return this.http.get<PilotFirstLastRace>(this.TEAM_URL.concat(this.REPORT_STATUS_ENDPOINT));
  }

  getPilotsReport() {
    return this.http.get<PilotFirstLastRace>(this.TEAM_URL.concat(this.REPORT_PILOTS_ENDPOINT));
  }

  getPilotSearch(searchTerm: string): Observable<PilotSearch[]> {
    return this.http.get<PilotSearch[]>(this.TEAM_URL.concat(this.SEARCH_PILOT_ENDPOINT).concat('?forename=').concat(searchTerm));
  }

  openSnackBar(message: string, color: string) {
    this._snackBar.open(message, 'Fechar', {
      duration: 5000,
      panelClass: [color],
      verticalPosition: 'top',
    });
  }
}
