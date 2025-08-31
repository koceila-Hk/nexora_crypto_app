import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../_services/AuthService';

@Component({
  selector: 'app-reset-password',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent {
  resetForm!: FormGroup;
  token!: string;
  message: string = '';
  submit = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Récupération du token depuis l’URL (query params)
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
    });

    // Formulaire pour saisir le nouveau mot de passe
    this.resetForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
    },
      { validators: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('newPassword')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  onResetPassword(): void {
    this.submit = true;

    if (this.resetForm.invalid) return;

    const newPassword = this.resetForm.value.newPassword;

    this.authService.resetPassword(this.token, newPassword).subscribe({
      next: (res) => {
        this.message = "Votre mot de passe a bien été réinitialisé";
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.message = "Le lien est invalide ou expiré";
      }
    });
  }
}
