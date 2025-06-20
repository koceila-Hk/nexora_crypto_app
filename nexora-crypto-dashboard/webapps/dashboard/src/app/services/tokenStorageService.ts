import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class TokenStorageService {
  private storage = sessionStorage; 

  getToken(): string | null {
    return this.storage.getItem('access_token');
  }

  saveToken(token: string): void {
    this.storage.setItem('access_token', token);
  }

  getUserIdFromToken(): number | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.userId || payload.sub || null;
    } catch (e) {
      console.error('Erreur de décodage du token', e);
      return null;
    }
  }


  clear(): void {
    this.storage.clear();
  }
}
