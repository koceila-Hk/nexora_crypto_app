import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TokenStorageService } from '../../_services/tokenStorageService';
import { AuthService } from '../../_services/AuthService';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  // isAuthenticated = false;
  isDarkMode = true;

  constructor(
    // private tokenStorage: TokenStorageService,
    private router: Router,
    private authService: AuthService) { }

  ngOnInit(): void {
    // this.authService.getCurrentUser().subscribe({
    //   next: () => {},
    //   error: () => this.authService._currentUser.set(null)
    // });
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
      this.isDarkMode = true;
      this.applyTheme('dark');
    } else {
      this.isDarkMode = false;
      this.applyTheme('light');
    }
  }

  toggleTheme(): void {
    this.isDarkMode = !this.isDarkMode;
    const theme = this.isDarkMode ? 'dark' : 'light';
    this.applyTheme(theme);
    localStorage.setItem('theme', theme);
  }

  applyTheme(theme: 'dark' | 'light'): void {
    const body = document.body;
    if (theme === 'dark') {
      body.classList.add('dark-theme');
      body.classList.remove('light-theme');
    } else {
      body.classList.add('light-theme');
      body.classList.remove('dark-theme');
    }
  }

  get currentThemeIcon(): string {
    return this.isDarkMode ? 'bi-sun-fill' : 'bi-moon-stars-fill';
  }

  get isAuthenticated(): boolean {
    return this.authService.isConnected();
  }

  logout(): void {
    // this.tokenStorage.signOut();
    this.authService.logout().subscribe(() => {
      this.router.navigate(['/home-not-auth']);
    })
  }

  navigateAccueil(): void {
    if (this.isAuthenticated) {
      this.router.navigate(['/home-auth']);
    } else {
      this.router.navigate(['/home-not-auth']);
    }
  }
}
