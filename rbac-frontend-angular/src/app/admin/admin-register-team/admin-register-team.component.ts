import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { AdminService } from '../admin.service';

@Component({
  selector: 'app-admin-register-team',
  templateUrl: './admin-register-team.component.html',
  styleUrls: ['./admin-register-team.css'],
})
export class AdminRegisterTeamComponent implements OnInit {
  form: FormGroup;
  
  constructor(
    private formBuilder: FormBuilder,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group(
      {
        constructorRef: new FormControl("", [Validators.minLength(4)]),
        name: new FormControl(""),
        nationality: new FormControl(""),
        url: new FormControl(""),
      },
      { validators: [Validators.required] }
    );
  }

  submit() {
    if (this.form.valid) {
      this.adminService.registerNewTeam(this.form.getRawValue());
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
