import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage = String;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.registerForm = this.fb.group({
      pseudonym: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {

    if (this.registerForm.valid) {
      this.http.post('http://localhost:8080/auth/signup', this.registerForm.value).subscribe({
        next: (res) => {
          console.log('Inscription réussi', res);
          sessionStorage.setItem('email', this.registerForm.value.email);
          this.router.navigate(['/verify']);
        },
        error: (err) => {
          console.error('Erreur lors de l’inscription', err);
          this.errorMessage = err.error;
        }
      });
    } else {
      console.warn('Formulaire invalide');
    }
  }
}  
