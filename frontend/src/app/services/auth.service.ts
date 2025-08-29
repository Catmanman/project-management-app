// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

interface AuthResponse { username: string; role: string; token: string; }

/**
 * Service handling user authentication. Tokens are persisted in localStorage
 * and appended to outgoing requests via the JWT interceptor. The service also
 * exposes helper methods to check login state and log the user out.
 *
 * NOTE: Backend URLs are intentionally hardcoded as requested.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  // Keep hardcoded URL
  private readonly authBase = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private router: Router) {}

  // ---- API calls ----
  login(username: string, password: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.authBase}/login`, { username, password })
      .pipe(tap(res => this.persist(res)));
  }

  register(username: string, password: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.authBase}/register`, { username, password })
      .pipe(tap(res => this.persist(res)));
  }

  // ---- Auth state ----
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  /**
   * Robust login check:
   * - ensures token exists
   * - verifies JWT structure
   * - checks `exp` and clears storage if expired/invalid
   */
  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const parts = token.split('.');
      if (parts.length !== 3) throw new Error('Bad JWT');

      const payloadJson = atob(parts[1]);
      const payload = JSON.parse(payloadJson);
      const now = Math.floor(Date.now() / 1000);

      if (payload?.exp && Number.isFinite(payload.exp) && payload.exp < now) {
        // Token expired -> clear and treat as logged out
        localStorage.removeItem('token');
        return false;
      }
      return true;
    } catch {
      // Malformed token -> clear and treat as logged out
      localStorage.removeItem('token');
      return false;
    }
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  // ---- Helpers ----
  private persist(res: AuthResponse) {
    localStorage.setItem('token', res.token);
    localStorage.setItem('username', res.username);
    localStorage.setItem('role', res.role);
  }
}
