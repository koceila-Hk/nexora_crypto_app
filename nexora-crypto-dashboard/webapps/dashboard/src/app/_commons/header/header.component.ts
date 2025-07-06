import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TokenStorageService } from '../../_services/tokenStorageService';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'], 
})
export class HeaderComponent implements OnInit {
  isAuthenticated = false;
  isDarkMode = true;

  constructor(private tokenStorage: TokenStorageService, private router: Router) {}

  ngOnInit(): void {
    this.isAuthenticated = this.tokenStorage.isLoggedIn();

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
    // Change l'icône en fonction du mode actuel
    return this.isDarkMode ? 'bi-sun-fill' : 'bi-moon-stars-fill';
  }

  logout(): void {
    this.tokenStorage.signOut();
    this.isAuthenticated = false;
    this.router.navigate(['/login']);
  }

  navigateAccueil(): void {
    if (this.isAuthenticated) {
      this.router.navigate(['/home-auth']);
    } else {
      this.router.navigate(['/home-not-auth']);
    }
  }
}
