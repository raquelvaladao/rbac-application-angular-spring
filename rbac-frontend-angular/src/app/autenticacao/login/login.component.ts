import { Component, OnInit } from "@angular/core";
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from "@angular/forms";
import { Router } from "@angular/router";
import { AutenticacaoService } from "../autenticacao.service";
import { valueOf } from "../model/Role";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./card.css"],
})
export class LoginComponent implements OnInit {
  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private autenticacaoService: AutenticacaoService,
    private router: Router,
    private _snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group(
      {
        login: new FormControl(""),
        password: new FormControl(""),
      },
      { validators: [Validators.required], updateOn: "change" }
    );
    if (localStorage.getItem("tokenscc")) this.router.navigate(["/home"]);
  }

  submit(): void {
    let token: string = "TOKEN!";

    if (this.form.valid) {
      this.autenticacaoService.autenticar(this.form.getRawValue()).subscribe({
        next: (json: any) => {
          token = json["token"];
          localStorage.setItem("tokenscc", token);

          let roleToken = JSON.parse(window.atob(token.split('.')[1]))['role'];
          localStorage.setItem("rolescc", valueOf(roleToken));

          let userName = JSON.parse(window.atob(token.split('.')[1]))['username'];
          localStorage.setItem("userscc", userName);

          this.router.navigate(["/home"]);
        },
        error: (error) => {
          if(error['status'] == '401' || error['status'] == '403'){
            this.openSnackBar();
          }
        },
      });
    }
  }

  errosInput(propNome: string) {
    if (this.form.get(propNome)?.hasError("naoAutorizado")) {
      return "Usuário ou senha incorretos";
    }

    if (this.form.get(propNome)?.hasError("required")) {
      return "Preenchimento obrigatório";
    }

    return null;
  }

  openSnackBar() {
    this._snackBar.open('Usuário ou senha incorretos', 'Fechar', {
      duration: 3000,
      panelClass: ['red-snackbar'],
      verticalPosition: 'top',
    });
  }
}
