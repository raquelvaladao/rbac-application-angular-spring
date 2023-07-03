import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html'
})
export class LogoutComponent implements OnInit {

  constructor(private route : Router) { }

  ngOnInit(): void {
    localStorage.removeItem('tokenscc');
    localStorage.removeItem('rolescc');
    localStorage.removeItem("userscc");

    this.route.navigate(['/login']); 
  }
}
