import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ValidationErrors,
  Validators,
} from "@angular/forms";
import { AdminService } from '../admin.service';


@Component({
  selector: 'app-admin-register-user',
  templateUrl: './admin-register-user.component.html',
  styleUrls: ['./admin-register-user.css'],
})
export class AdminRegisterUserComponent implements OnInit {

  form: FormGroup;
  tipos: string[] = ["ADMIN", "ESCUDERIA", "PILOTO"];
  constructor(
    private formBuilder: FormBuilder,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group(
      {
        driverRef: new FormControl("", [Validators.minLength(4)]),
        number: new FormControl("", [
          Validators.minLength(1),
          Validators.maxLength(50),
        ]),
        code: new FormControl(""),
        forename: new FormControl(""),
        surname: new FormControl(""),
        birthDate: new FormControl("", [Validators.pattern(/^\d{4}\-(0[1-9]|1[012])\-(0[1-9]|[12][0-9]|3[01])$/)]),
        nationality: new FormControl("")
      },
      { validators: [Validators.required] }
    );
  }

  submit() {
    if (this.form.valid) {
      this.adminService.registerNewPilot(this.form.getRawValue());
    }
  }

  errosInput(propNome: string) {
    if (this.form.get(propNome)?.hasError("required")) {
      return "Preenchimento obrigatório";
    }
    if (this.form.get(propNome)?.hasError("pattern")) {
      return "Data deve estar no formato yyyy-MM-dd";
    }
    if (this.form.get(propNome)?.hasError("minlength")) {
      let erros = this.form.controls[propNome].errors as ValidationErrors;

      return `Pelo menos ${erros["minlength"]["requiredLength"]} letras`;
    }

    if (this.form.get(propNome)?.hasError("maxlength")) {
      let erros = this.form.controls[propNome].errors as ValidationErrors;

      return `No máximo ${erros["maxlength"]["requiredLength"]} letras`;
    }

    return null;
  }
}
