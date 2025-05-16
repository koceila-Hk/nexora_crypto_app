import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      console.log('Sending data:', this.registerForm.value);
      this.http.post('http://localhost:8080/auth/signup', this.registerForm.value).subscribe({
        next: (res) => {
          console.log('Inscription réussie', res);
        },
        error: (err) => {
          console.error('Erreur lors de l’inscription', err);
        }
      });
    } else {
      console.warn('Formulaire invalide');
    }
  }
}  
