import { Component, Input, OnInit } from "@angular/core";
import { Router } from "@angular/router";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./hover.css"],
})
export class HomeComponent implements OnInit {
  userName: string | null;
  isToken: string | null;
  roleStorage: string | null;

  constructor(private router: Router) {
    this.userName = localStorage.getItem('username')
  }

  ngOnInit(): void {
    this.isToken = localStorage.getItem("tokenscc") as string;
    this.roleStorage = localStorage.getItem("rolescc") as string;
    this.userName = localStorage.getItem("userscc") as string;

    if(!this.isToken) {
      this.router.navigate(['/login']); 
    }
  }

  navigateOnRole(path: string): void {
    const role = localStorage.getItem('rolescc');
    const route = `${role}/${path}`;
  
    this.router.navigate([route]);
  }
}
