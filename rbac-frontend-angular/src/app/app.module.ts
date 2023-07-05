import { APP_BASE_HREF } from "@angular/common";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from "@angular/material/menu";
import { MatSelectModule } from "@angular/material/select";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterModule } from "@angular/router";
import { BsDropdownModule } from "ngx-bootstrap/dropdown";
import { AppComponent } from "./app.component";
import { rootRouterConfig } from "./app.routes";
import { AutenticacaoInterceptorService } from "./autenticacao/autenticacao-interceptor.service";
import { AutenticacaoService } from "./autenticacao/autenticacao.service";
import { LoginComponent } from "./autenticacao/login/login.component";
import { LogoutComponent } from "./autenticacao/logout/logout.component";
import { RegistrarComponent } from './autenticacao/registrar/registrar.component';
import { FooterComponent } from "./navegacao/footer/footer.component";
import { HomeComponent } from "./navegacao/home/home.component";
import { MenuComponent } from "./navegacao/menu/menu.component";
import { LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { PilotOverviewComponent } from './pilot/pilot-overview/pilot-overview.component';
import { AdminOverviewComponent } from './admin/admin-overview/admin-overview.component';
import { TeamOverviewComponent } from './team/team-overview/team-overview.component';
import { AdminRegisterUserComponent } from "./admin/admin-register-user/admin-register-user.component";
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar'; 
import { MatButtonModule } from '@angular/material/button'
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { MatTableModule } from '@angular/material/table';
import { PositionComponent } from './admin/admin-reports/position/position.component';
import { CitiesComponent } from './admin/admin-reports/cities/cities.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import { StatusComponent } from './pilot/pilot-reports/status/status.component';
import { RollupComponent } from './pilot/pilot-reports/rollup/rollup.component';
import { TeamStatusComponent } from './team/team-reports/team-status/team-status.component';
import { TeamPilotsComponent } from './team/team-reports/team-pilots/team-pilots.component';

registerLocaleData(localePt);
@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    FooterComponent,

    HomeComponent,
    LoginComponent,
    LogoutComponent,

    RegistrarComponent,

    AdminOverviewComponent,
    AdminRegisterUserComponent,
    PositionComponent,

    PilotOverviewComponent,

    TeamOverviewComponent,
    ForbiddenComponent,
    PositionComponent,
    CitiesComponent,
    StatusComponent,
    RollupComponent,
    TeamStatusComponent,
    TeamPilotsComponent,
    
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    BsDropdownModule,
    MatSelectModule,
    BrowserAnimationsModule,
    MatMenuModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatSnackBarModule,
    MatTableModule,
    MatPaginatorModule,
    MatDatepickerModule,
    MatNativeDateModule,
    [RouterModule.forRoot(rootRouterConfig, { useHash: false })],
  ],
  providers: [
    { provide: APP_BASE_HREF, useValue: "/" },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AutenticacaoInterceptorService,
      multi: true,
    },
    {provide: LOCALE_ID, useValue: 'pt-BR'},
    AutenticacaoService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
