import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {firstValueFrom} from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable({providedIn: 'root'})
export class AuthService {
  isAuthenticated = signal(false);
  http = inject(HttpClient);
  router = inject(Router);
  private readonly authApiUrl = environment.apiUrl;
  private token: string | null = null;

  constructor() {
    this.checkSession();
  }

  async login(username: string, password: string): Promise<boolean> {
    if (this.isAuthenticated()) {
      this.logout();
    }
    try {
      if (this.authApiUrl !== 'http://nonexistent') { // For testing, pretend logged in if url is this
        const response = await firstValueFrom(
          this.http.post<{ token: string }>(`${this.authApiUrl}/login`, {username, password})
        );

        this.token = response.token;
        this.isAuthenticated.set(true);
        localStorage.setItem('auth_token', this.token);
        return true;
      } else {
        console.log("Hackerman logged in for you")
        this.isAuthenticated.set(true);
        return true;
      }
    } catch (error: any) {
      alert('Login failed: ' + error.message);
      console.error('Login failed:', error);
      return false;
    }
  }

  async register(username: string, password: string): Promise<boolean> {
    if (this.isAuthenticated()) {
      this.logout();
    }
    try {
      if (this.authApiUrl !== 'http://nonexistent') {
        const response = await firstValueFrom(
          this.http.post<{ token: string }>(`${this.authApiUrl}/register`, {username, password})
        );

        this.token = response.token
        this.isAuthenticated.set(true);
        localStorage.setItem('auth_token', this.token);
        return true;
      } else {
        console.log("Hackerman registerd AND logged in for you, what cant he do?? oO")
        this.isAuthenticated.set(true);
        return true;
      }
    } catch (error: any) {
      alert('Register failed: ' + error.message);
      console.error('Register failed:', error);
      this.isAuthenticated.set(false);
      return false;
    }
  }

  logout(): void {
    this.token = null;
    this.isAuthenticated.set(false);
    localStorage.removeItem('auth_token');

    try {
      this.http.post<void>(`${this.authApiUrl}/logout`, {});
    } catch (error) {
      console.error('Logout failed:', error);
    }
  }

  getAuthToken(): string | null {
    return this.token;
  }

  getToken(): string | null {
    return this.token;
  }

  setToken(token: string): void {
    this.token = token;
  }

  clearToken(): void {
    this.token = null;
  }

  private checkSession(): void {
    const storedToken = localStorage.getItem('auth_token');
    if (storedToken) {
      this.token = storedToken;
      this.isAuthenticated.set(true);
    } else {
      this.isAuthenticated.set(false);
    }
  }
}
