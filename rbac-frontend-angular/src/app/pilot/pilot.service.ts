import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PilotService {

  PILOT_URL = 'http://localhost:8080/pilot'
  OVERVIEW_ENDPOINT = '/overview'
  REPORT_POSITION_ENDPOINT = '/report/victories'

  constructor(
    private http: HttpClient,
    private _snackBar: MatSnackBar,
    ) { }

    getOverview(): Observable<any> {
      return this.http.get(this.PILOT_URL.concat(this.OVERVIEW_ENDPOINT));
    }

    openSnackBar(message: string, color: string) {
      this._snackBar.open(message, 'Fechar', {
        duration: 5000,
        panelClass: [color],
        verticalPosition: 'top',
      });
    }
}
