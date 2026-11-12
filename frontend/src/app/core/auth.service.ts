import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { LoginResponse, Role } from '../models/clinic.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly base = 'http://localhost:8080/api';
  readonly role = signal<Role | null>(this.readRole());
  readonly email = signal<string | null>(localStorage.getItem('clinic.email'));
  constructor(private http: HttpClient) {}
  token(): string | null { return localStorage.getItem('clinic.token'); }
  isAuthenticated(): boolean { return !!this.token() && !!this.role(); }
  login(email: string, password: string) {
    return this.http.post<LoginResponse>(`${this.base}/auth/login`, { email, password }).pipe(tap(result => {
      localStorage.setItem('clinic.token', result.token); localStorage.setItem('clinic.role', result.role);
      localStorage.setItem('clinic.email', result.email); this.role.set(result.role); this.email.set(result.email);
    }));
  }
  logout(): void { localStorage.removeItem('clinic.token'); localStorage.removeItem('clinic.role'); localStorage.removeItem('clinic.email'); this.role.set(null); this.email.set(null); }
  private readRole(): Role | null { const value = localStorage.getItem('clinic.role'); return value === 'ADMIN' || value === 'PATIENT' ? value : null; }
}
