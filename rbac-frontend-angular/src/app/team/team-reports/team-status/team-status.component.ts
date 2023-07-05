import { Component, OnInit, ViewChild } from '@angular/core';
import { TeamServiceService } from '../../team-service.service';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { StatusReportTuple } from 'src/app/autenticacao/model/PositionReportTuple';

@Component({
  selector: 'app-team-status',
  templateUrl: './team-status.component.html',
  styleUrls: ['./status.css'],
})
export class TeamStatusComponent implements OnInit {
  pageSizeOptions = [5, 10, 25];
  showPageSizeOptions = true;
  showFirstLastButtons = true;
  
  displayedColumns: string[] = ['name', 'symbol'];
  data: StatusReportTuple[] = [];
  
  src: MatTableDataSource<StatusReportTuple[]>;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    private teamService: TeamServiceService,
    private customPaginatorIntl: MatPaginatorIntl
  ) {
    this.customPaginatorIntl.itemsPerPageLabel = 'Itens por página:';
    this.customPaginatorIntl.nextPageLabel = 'Próxima página';
    this.customPaginatorIntl.previousPageLabel = 'Página anterior';
    this.customPaginatorIntl.firstPageLabel = 'Primeira página';
    this.customPaginatorIntl.lastPageLabel = 'Última página';
  }

  ngOnInit(): void {
    this.getData();
  }

  private getData() {
    this.teamService.getStatus().subscribe({
      next: (response: any) => {
        this.src = new MatTableDataSource<any>(response.body);
        this.src.paginator = this.paginator;
        this.paginator._intl = this.customPaginatorIntl;
      },
      error: () => {
        this.teamService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }

}
