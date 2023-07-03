import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { valueOf } from "./model/Role";

@Injectable({
  providedIn: "root",
})
export class AutenticacaoService {
  protected URL_AUTH: string = "http://localhost:8080/auth";
  protected URL_REGISTRAR: string = "http://localhost:8080/auth/new";

  constructor(private http: HttpClient, private router: Router) {}

  public autenticar(credenciais: any) {

    return this.http
      .post(this.URL_AUTH, credenciais, { withCredentials: true });
  }

  public registrar(request: any) {
    this.http.post(this.URL_REGISTRAR, request).subscribe((json: any) => {
      
      let token = json["token"];
      localStorage.setItem("tokenscc", token);
      let roleToken = JSON.parse(window.atob(token.split('.')[1]));
      localStorage.setItem("rolescc", valueOf(roleToken).toLocaleLowerCase());

      alert("Cadastrado com sucesso! Você será redirecionado!")
      this.router.navigate(["/home"]);
    });
  }
 
}
