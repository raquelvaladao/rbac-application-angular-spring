import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, CanDeactivate, CanLoad, Route, Router, RouterStateSnapshot, UrlSegment, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardGuard implements CanActivate {

  constructor(private router: Router){}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    const token = localStorage.getItem("tokenscc");
    const role = localStorage.getItem("rolescc");
   
    if(isNotLoggedAndValid(token, state)) {
      return this.router.parseUrl("/forbidden");
    }

    const rolePath = role?.toLocaleLowerCase() === 'ADMINISTRADOR' ? 'admin' : role?.toLocaleLowerCase();

    if(state.url.split('/')[1] != rolePath) {
      return this.router.parseUrl("/forbidden");
    }

    return true;
  }
}
function isNotLoggedAndValid(token: string | null, state: RouterStateSnapshot) {
  return (token == null || token == '') && (state.url != 'login' && state.url != 'home');
}

