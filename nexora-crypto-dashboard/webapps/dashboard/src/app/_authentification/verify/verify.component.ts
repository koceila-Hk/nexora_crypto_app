import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './verify.component.html'
})
export class VerifyComponent implements OnInit {
  verifyForm: FormGroup;
  email: string | undefined;

  constructor(
    private fb: FormBuilder, 
    private http: HttpClient, 
    private router: Router,
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
    if (this.verifyForm.invalid) return;

    const payload = {
      verificationCode: this.verifyForm.value.verificationCode,
      email: this.email
      
    };

    this.http.post('http://localhost:8080/auth/verify', payload).subscribe({
      next: (res) => {
        console.log('Vérification réussie', res);
        sessionStorage.removeItem('email');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Échec de la vérification', err)
      } 
    });
  }
}
