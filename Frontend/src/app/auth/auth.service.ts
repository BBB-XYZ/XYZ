import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({providedIn: 'root'})
export class AuthService {
  public readonly isAuthenticated = signal(false);
  private http = inject(HttpClient);
  private readonly API_URL = environment.apiUrl;
  private readonly AUTH_TOKEN_KEY = 'auth_token';
  private token: string | null = null;

  public constructor() {
    this.initializeSession();
  }

  public async login(username: string, password: string): Promise<boolean> {
    if (this.isAuthenticated()) {
      this.logout();
    }
    try {
      const response = await firstValueFrom(
        this.http.post<{ token: string }>(`${this.API_URL}/login`, {username, password})
      );

      this.token = response.token;
      this.isAuthenticated.set(true);
      localStorage.setItem(this.AUTH_TOKEN_KEY, this.token);
      return true;
    } catch (error: any) {
      alert('Login failed: ' + error.message);
      return false;
    }
  }

  public async register(username: string, password: string): Promise<boolean> {
    if (this.isAuthenticated()) {
      this.logout();
    }
    try {
      const response = await firstValueFrom(
        this.http.post<{ token: string }>(`${this.API_URL}/register`, {username, password})
      );

      this.token = response.token
      this.isAuthenticated.set(true);
      localStorage.setItem(this.AUTH_TOKEN_KEY, this.token);
      return true;
    } catch (error: any) {
      alert('Register failed: ' + error.message);
      this.isAuthenticated.set(false);
      return false;
    }
  }

  public logout(): void {
    this.token = null;
    this.isAuthenticated.set(false);
    this.removeToken();

    try {
      this.http.post<void>(`${this.API_URL}/logout`, {});
    } catch (error) {
      console.error('Logout failed:', error);
    }
  }

  public getToken(): string | null {
    return this.token;
  }

  public setToken(token: string): void {
    this.token = token;
  }

  public removeToken(): void {
    localStorage.removeItem(this.AUTH_TOKEN_KEY);
    this.token = null;
  }

  private initializeSession(): void {
    const storedToken = localStorage.getItem(this.AUTH_TOKEN_KEY);
    if (storedToken) {
      this.token = storedToken;
      this.isAuthenticated.set(true);
    } else {
      this.isAuthenticated.set(false);
    }
  }
}
