import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { TokenStorageService } from '../../_services/tokenStorageService';
import { environment } from '../../../environments/envionment';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage!: String;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private tokenStorage: TokenStorageService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {

    if (this.loginForm.valid) {
      this.http.post(environment.apiUrl + '/auth/login', this.loginForm.value).subscribe({
        next: (res: any) => {
          // console.log('response :', res)
          const token = res.token;
          const refreshToken = res.refreshToken;
          this.tokenStorage.saveToken(token);
          this.tokenStorage.saveRefreshToken(refreshToken);
          this.router.navigate(['/home-auth']);
        },
        error: (err) => {
          console.error('Erreur lors de l\'authentificaiton', err);
          this.errorMessage = 'Erreur lors de l\'authentificaiton';
        }
      });
    } else {
      console.warn('Formulaire invalide');
    }
  }
}  
