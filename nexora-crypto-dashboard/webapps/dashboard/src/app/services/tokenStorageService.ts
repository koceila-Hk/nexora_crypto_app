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

  clear(): void {
    this.storage.clear();
  }
}
