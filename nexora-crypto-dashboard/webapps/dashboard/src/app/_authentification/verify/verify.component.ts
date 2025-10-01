import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/envionment';

@Component({
  selector: 'app-verify',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './verify.component.html',
  styleUrl: './verify.component.css'
})
export class VerifyComponent implements OnInit {
  verifyForm: FormGroup;
  email: string | undefined;
  submitted = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.verifyForm = this.fb.group({
      verificationCode: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.email = sessionStorage.getItem('email') || '';
    if (!this.email) {
      this.router.navigate(['/register']);
    }
  }

  onSubmit() {
    this.submitted = true;

    if (this.verifyForm.invalid) return;

    const payload = {
      verificationCode: this.verifyForm.value.verificationCode,
      email: this.email
    };

    this.http.post(environment.apiUrl + '/auth/verify', payload).subscribe({
      next: () => {
        this.successMessage = 'Vérification réussie. Redirection vers la page de connexion...';
        this.errorMessage = '';
        sessionStorage.removeItem('email');

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (err) => {
        this.errorMessage = err.error.message;
      }
    });
  }
}
