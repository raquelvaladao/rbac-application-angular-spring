import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

@Component({
  selector: "app-menu",
  templateUrl: "./menu.component.html",
  styleUrls: ['./menu.css'],
})
export class MenuComponent implements OnInit {
  isToken: string | null = null;
  role: string | undefined;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.router.events.subscribe((val: any) => {
      this.isToken = localStorage.getItem("tokenscc");
      this.role = localStorage.getItem("rolescc")?.toUpperCase();
    });
    this.isToken = localStorage.getItem("tokenscc");
    this.role = localStorage.getItem("rolescc")?.toUpperCase();
  }
}
