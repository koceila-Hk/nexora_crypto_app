import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TokenStorageService } from '../../_services/tokenStorageService';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {
  isAuthenticated = false;
  isDarkMode = true;

  toggleTheme() {
    this.isDarkMode = !this.isDarkMode;
    const body = document.body;
    if (this.isDarkMode) {
      body.classList.remove('light-theme');
      body.classList.add('dark-theme');
    } else {
      body.classList.remove('dark-theme');
      body.classList.add('light-theme');
    }
  }

  get currentThemeIcon(): string {
    return this.isDarkMode ? 'bi-sun-fill' : 'bi-moon-stars-fill';
  }


  constructor(private tokenStorage: TokenStorageService, private router: Router) {}

  ngOnInit(): void {
    this.isAuthenticated = this.tokenStorage.isLoggedIn();
  }

  logout(): void {
    this.tokenStorage.signOut();
    this.isAuthenticated = false;
    this.router.navigate(['/login']);
  }
}
