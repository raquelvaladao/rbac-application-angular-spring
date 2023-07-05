import { Routes } from "@angular/router";
import { LoginComponent } from "./autenticacao/login/login.component";
import { LogoutComponent } from "./autenticacao/logout/logout.component";
import { RegistrarComponent } from "./autenticacao/registrar/registrar.component";
import { HomeComponent } from "./navegacao/home/home.component";
import { PilotOverviewComponent } from "./pilot/pilot-overview/pilot-overview.component";
import { AdminOverviewComponent } from "./admin/admin-overview/admin-overview.component";
import { AdminRegisterUserComponent } from "./admin/admin-register-user/admin-register-user.component";
import { TeamOverviewComponent } from "./team/team-overview/team-overview.component";
import { AuthGuardGuard } from "./auth-guard.guard";
import { ForbiddenComponent } from "./forbidden/forbidden.component";
import { PositionComponent } from "./admin/admin-reports/position/position.component";
import { CitiesComponent } from "./admin/admin-reports/cities/cities.component";
import { StatusComponent } from "./pilot/pilot-reports/status/status.component";
import { TeamStatusComponent } from "./team/team-reports/team-status/team-status.component";
import { TeamPilotsComponent } from "./team/team-reports/team-pilots/team-pilots.component";

export const rootRouterConfig: Routes = [
  { path: "", redirectTo: "/home", pathMatch: "full" },
  { path: "home", component: HomeComponent },
  { path: "login", component: LoginComponent, data: { title: "Login" } },
  { path: "logout", component: LogoutComponent },
  { path: "registrar", component: RegistrarComponent },
  { path: "forbidden", component: ForbiddenComponent },
  {
    path: "produtos",
    redirectTo: "produtos/listar",
  },
  {
    path: "admin",
    canActivate: [AuthGuardGuard],
    children: [
      {
        path: "relatorios",
        children: [
          {
            path: "status",
            component: PositionComponent,
          },
          {
            path: "cidades",
            component: CitiesComponent,
          },
        ]
      },
      {
        path: "overview",
        component: AdminOverviewComponent,
      },
      {
        path: "criar",
        component: AdminRegisterUserComponent,
      },
    ],
  },
  {
    path: "piloto",
    canActivate: [AuthGuardGuard],
    children: [
      {
        path: "relatorios",
        children: [
          {
            path: "status",
            component: StatusComponent,
          },
        ]
      },
      {
        path: "overview",
        component: PilotOverviewComponent,
      },
    ],
  },
  {
    path: "escuderia",
    canActivate: [AuthGuardGuard],
    children: [
      {
        path: "relatorios",
        children: [
          {
            path: "status",
            component: TeamStatusComponent,
          },
          {
            path: "pilotos",
            component: TeamPilotsComponent,
          },
        ]
      },
      {
        path: "overview",
        component: TeamOverviewComponent,
      },
    ],
  },
];
