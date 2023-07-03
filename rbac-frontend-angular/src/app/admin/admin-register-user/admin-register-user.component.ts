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
  styleUrls: ['./admin-register-user.css']
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
        login: new FormControl("", [Validators.minLength(4)]),
        password: new FormControl("", [
          Validators.minLength(3),
          Validators.maxLength(50),
        ]),
        type: new FormControl("", {
          validators: [Validators.required],
          updateOn: "blur",
        }),
      },
      { validators: [Validators.required] }
    );
  }

  submit() {
    if (this.form.valid) {
      this.adminService.registerNewUser(this.form.getRawValue());
    }
  }

  errosInput(propNome: string) {
    if (this.form.get(propNome)?.hasError("required")) {
      return "Preenchimento obrigatório";
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
