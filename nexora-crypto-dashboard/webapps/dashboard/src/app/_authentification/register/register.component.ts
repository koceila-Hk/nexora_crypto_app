import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl,
  ValidationErrors,
  ReactiveFormsModule
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environments/envionment';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string = '';
  infoMessage: string = '';
  submit = false;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.registerForm = this.fb.group(
      {
        pseudonym: ['', [Validators.required, Validators.minLength(2)]],
        email: ['', [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]],
        password: ['', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?":{}|<>]).{12,}$')]],
        confirmPassword: ['', Validators.required],
        acceptTerms: [false, Validators.requiredTrue],
      },
      { validators: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  onSubmit() {
    // console.log('click')
    this.submit = true;
    this.registerForm.markAllAsTouched();

    if (this.registerForm.valid) {
      this.http.post(environment.apiUrl + '/auth/signup', this.registerForm.value).subscribe({
        next: (res) => {
          this.infoMessage = "Vous allez recevoir un code de vérification par email.";
          sessionStorage.setItem('email', this.registerForm.value.email);
          setTimeout(() => {
            this.router.navigate(['/verify']);
          }, 4000);
        },
        error: () => {
          this.infoMessage = "Vous allez recevoir un code de vérification par email.";
          sessionStorage.setItem('email', this.registerForm.value.email);
          setTimeout(() => {
            this.router.navigate(['/verify']);
          }, 4000);
        },
      });
    }
  }
}
