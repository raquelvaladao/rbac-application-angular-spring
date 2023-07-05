import { Component, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { PilotServiceService } from '../../pilot-service.service';
import { SummaryYear } from 'src/app/autenticacao/model/SummaryYear';
import { YearRaceVictory } from 'src/app/autenticacao/model/YearRaceVictory';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Total } from 'src/app/autenticacao/model/Total';

@Component({
  selector: 'app-rollup',
  templateUrl: './rollup.component.html',
  styleUrls: ['./rollup.css']
})
export class RollupComponent implements OnInit {
  pageSizeOptions = [5, 10, 25];
  showPageSizeOptions = true;
  showFirstLastButtons = true;
  
  displayedColumns: string[] = ['year', 'quantity'];
  displayedColumnsTotal: string[] = ['total'];
  displayedColumnsAll: string[] = ['race', 'year', 'quantity'];

  summary: SummaryYear[] = [];
  allVictories: YearRaceVictory[] = [];
  total: Total[] = [];
  
  summarySrc: MatTableDataSource<SummaryYear[]>;
  allVictoriesSrc: MatTableDataSource<YearRaceVictory[]>;
  totalSrc: MatTableDataSource<Total[]>;

  @ViewChildren(MatPaginator) paginator = new  QueryList<MatPaginator>();

  constructor(
    private pilotService: PilotServiceService,
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
    this.pilotService.getRollupVictories().subscribe({
      next: (response: any) => {
        this.summarySrc = new MatTableDataSource<any>(response.body.summary);
        this.summarySrc.paginator = this.paginator.toArray()[0];
        this.paginator.toArray()[0]._intl = this.customPaginatorIntl;

        this.allVictoriesSrc = new MatTableDataSource<any>(response.body.allVictories);
        this.allVictoriesSrc.paginator = this.paginator.toArray()[1];
        this.paginator.toArray()[1]._intl = this.customPaginatorIntl;

        console.log(response.body.total)
        this.total.push({total: response.body.total});
        this.totalSrc = new MatTableDataSource<any>(this.total);
        
      },
      error: () => {
        this.pilotService.openSnackBar('Não foi possível carregar os dados.', 'red-snackbar');
      }
    });
  }

}
