import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AutenticacaoInterceptorService implements HttpInterceptor {

  private token: string;

  constructor(private router: Router) {
      
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.token = localStorage.getItem('tokenscc') as string;
    if (this.token) {
      const modReq = request.clone({
        headers: request.headers.set("Authorization",
        "Bearer " + this.token)
      });
      return next.handle(modReq);
    }
      return next.handle(request);
  }


 


}
