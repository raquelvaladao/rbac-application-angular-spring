import { Component, OnInit, ViewChild } from '@angular/core';
import { AdminService } from '../../admin.service';
import { PositionReportTuple } from 'src/app/autenticacao/model/PositionReportTuple';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';


@Component({
  selector: 'app-position',
  templateUrl: './position.component.html',
  styleUrls: ['./position.css'],
})
export class PositionComponent implements OnInit {
  pageSizeOptions = [5, 10, 25];
  showPageSizeOptions = true;
  showFirstLastButtons = true;
  
  displayedColumns: string[] = ['demo-name', 'demo-symbol'];
  data: PositionReportTuple[] = [];
  
  src: MatTableDataSource<PositionReportTuple[]>;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    private adminService: AdminService,
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
    this.adminService.getPositionReport().subscribe({
      next: (response: any) => {
        this.src = new MatTableDataSource<any>(response.body);
        this.src.paginator = this.paginator;
        this.paginator._intl = this.customPaginatorIntl;
      },
      error: () => {
        this.adminService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }
}
