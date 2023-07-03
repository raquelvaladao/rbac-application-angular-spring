import { HttpClient } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import {
  FormGroup,
  FormBuilder,
  FormControl,
  Validators,
  ValidationErrors,
  ValidatorFn,
  AbstractControl,
} from "@angular/forms";
import { AutenticacaoService } from "../autenticacao.service";

@Component({
  selector: "app-registrar",
  templateUrl: "./registrar.component.html",
  styles: [
    `
    .full-width-field {
      width: 100%;
   }
    `
  ]
})
export class RegistrarComponent implements OnInit {
  form: FormGroup;
  cpfSucesso: boolean | null;

  CPF_URL =
    "https://api.invertexto.com/v1/validator?token=333%7Ctb08rjo5vlly8JLk3qJbMkE1y4PKuoIZ&value=";
  FIM_CPF_URL = "&type=cpf";

  constructor(
    private formBuilder: FormBuilder,
    private autenticacaoService: AutenticacaoService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group(
      {
        nome: new FormControl(""),
        cpf: new FormControl("", {
          validators: [this.cpfInvalidoValidator()],
          updateOn: "blur"
        }),
        login: new FormControl("", [
          Validators.email,
          Validators.minLength(5),
          Validators.maxLength(20),
        ]),
        senha: new FormControl("", [Validators.minLength(6)]),
      },
      { validators: [Validators.required], updateOn: "submit", }
    );
  }

  submit() {
    if (this.form.valid) {
      this.autenticacaoService.registrar(this.form.getRawValue());
    }
  }

  errosInput(propNome: string) {
    if (this.form.get(propNome)?.getError("cpfInvalido") == "true") {
      return "CPF INVÁLIDO";
    }
    if (this.form.get(propNome)?.hasError("required")) {
      return "Preenchimento obrigatório";
    }
    if (this.form.get(propNome)?.hasError("minlength")) {
      let erros = this.form.controls[propNome].errors as ValidationErrors;

      return `Pelo menos ${erros["minlength"]["requiredLength"]} letras`;
    }

    return null;
  }

  cpfInvalidoValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      let isCPFInvalido = false;

      if (control.value.length > 0)
        isCPFInvalido = this.cpfInvalitoRequest(control, isCPFInvalido);

      return isCPFInvalido ? { cpfInvalido: true } : null;
    };
  }

  private cpfInvalitoRequest(control: AbstractControl, forbidden: boolean) {
    this.http
      .get(this.CPF_URL + control.value + this.FIM_CPF_URL)
      .subscribe((isValido: any) => {
        if (isValido["valid"] == false ) {
          forbidden = true;
          this.form.get("cpf")?.setErrors({ cpfInvalido: "true" });
          this.cpfSucesso = false;
        } else {
          this.cpfSucesso = true;
        }
      });
    return forbidden;
  }
}
