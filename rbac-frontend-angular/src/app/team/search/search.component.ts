import { Component, OnInit, ViewChild } from '@angular/core';
import { PilotSearch } from 'src/app/autenticacao/model/PilotSearch';
import { TeamServiceService } from '../team-service.service';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.css'],
})
export class SearchComponent implements OnInit {
  pageSizeOptions = [5, 10, 25];
  showPageSizeOptions = true;
  showFirstLastButtons = true;
  searchTerm = '';
  
  displayedColumns: string[] = ['name', 'birth', 'nationality'];
  data: PilotSearch[] = [];
  
  src: MatTableDataSource<PilotSearch[]>;

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
    
  }

  getData() {
    if (this.searchTerm.trim() !== '') {
      this.teamService.getPilotSearch(this.searchTerm).subscribe({
        next: (response: any) => {
          this.data = response.body;
          this.src = new MatTableDataSource<any>(this.data);
          this.src.paginator = this.paginator;
          this.paginator._intl = this.customPaginatorIntl;
        },
        error: () => {
          this.teamService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
        }
      });
    } else {
      this.data = [];
      this.src = new MatTableDataSource<any>(this.data);
    }
    
    
  }
}
