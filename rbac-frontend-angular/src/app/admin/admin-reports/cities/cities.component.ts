import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { AdminService } from '../../admin.service';
import { CityReport } from '../../../autenticacao/model/CityReport';

@Component({
  selector: 'app-cities',
  templateUrl: './cities.component.html',
  styleUrls: ['./cities.css'],

})
export class CitiesComponent implements OnInit {
  pageSizeOptions = [5, 10, 25];
  showPageSizeOptions = true;
  showFirstLastButtons = true;
  searchTerm = '';
  
  displayedColumns: string[] = ['cityName', 'iataCode', 'airName', 'airType', 'distanceRounded'];
  data: CityReport[] = [];
  
  src: MatTableDataSource<CityReport[]>;

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
    
  }

  getData() {
    if (this.searchTerm.trim() !== '') {
      this.adminService.getCitiesReport(this.searchTerm).subscribe({
        next: (response: any) => {
          this.data = response.body;
          this.src = new MatTableDataSource<any>(this.data);
          this.src.paginator = this.paginator;
          this.paginator._intl = this.customPaginatorIntl;
        },
        error: () => {
          this.adminService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
        }
      });
    } else {
      this.data = [];
      this.src = new MatTableDataSource<any>(this.data);
    }
    
    
  }

}
