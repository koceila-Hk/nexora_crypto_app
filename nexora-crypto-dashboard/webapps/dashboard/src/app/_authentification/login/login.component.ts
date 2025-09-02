import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../_services/AuthService';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  forgotForm: FormGroup;
  resetForm: FormGroup;
  errorMessage!: string;
  message!: string;
  submit = false;
  showForgot = false;
  showReset = false;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]],
      password: ['', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?":{}|<>]).{12,}$')]]
    });

    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]]
    });

    this.resetForm = this.fb.group({
      newPassword: ['', Validators.required]
    });

    // Vérifie si un token est présent dans l'URL pour afficher le formulaire de reset
    const token = this.route.snapshot.queryParamMap.get('token');
    if (token) {
      this.showReset = true;
    }
  }

  // Connexion
  onSubmit() {
    this.submit = true;
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: () => this.router.navigate(['home-auth']),
        error: () => this.errorMessage = 'Erreur lors de l\'authentification'
      });
    }
  }

  // Afficher / masquer formulaire mot de passe oublié
  toggleForgot() {
    console.log('password click', this.showForgot);
    this.showForgot = !this.showForgot;
  }

  // Mot de passe oublié
  onForgotPassword() {
    this.submit = true;
    if (this.forgotForm.valid) {
      this.authService.forgotPassword(this.forgotForm.value.email).subscribe({
        next: res => this.message = res.message,
        error: err => this.message = err.error
      });
    }
  }

  // Réinitialiser mot de passe
  onResetPassword() {
    this.submit = true;
    const token = this.route.snapshot.queryParamMap.get('token');
    if (this.resetForm.valid && token) {
      this.authService.resetPassword(token, this.resetForm.value.newPassword).subscribe({
        next: res => this.message = res.message,
        error: err => this.message = err.error
      });
    }
  }
}
