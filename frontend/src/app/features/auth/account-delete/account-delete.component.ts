import { Component, computed, effect, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '@services/user.service';

function passwordsMatch(group: AbstractControl): ValidationErrors | null {
  const newPass = group.get('newPassword')?.value;
  const confirm = group.get('confirm')?.value;
  return newPass && confirm && newPass !== confirm ? { mismatch: true } : null;
}

@Component({
  selector: 'app-account-delete',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './account-delete.component.html',
})
export class AccountDeleteComponent {
  private fb = inject(FormBuilder);
  private users = inject(UserService);
  private router = inject(Router);

  // ---- Role detection (from localStorage if you store it there) ----
  private storedRoles = (() => {
    try { return JSON.parse(localStorage.getItem('roles') || '[]'); } catch { return []; }
  })();
  roles = signal<string[]>(Array.isArray(this.storedRoles) ? this.storedRoles : []);
  isAdmin = computed(() => this.roles().some(r => r === 'ROLE_ADMIN' || r === 'ADMIN'));

  // ---- Self-delete form ----
  selfForm = this.fb.group({
    confirmPhrase: ['', [Validators.required]],
  });
  confirmPhraseRequired = 'delete my account';

  // ---- Change username form (requires current password) ----
  usernameForm = this.fb.group({
    currentPassword: ['', [Validators.required]],
    newUsername: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
  });

  // ---- Change password form (requires current password + confirm) ----
  passwordForm = this.fb.group({
    currentPassword: ['', [Validators.required]],
    newPassword: ['', [Validators.required, Validators.minLength(8)]],
    confirm: ['', [Validators.required]],
  }, { validators: passwordsMatch });

  busy = signal(false);
  message = signal<string | null>(null);
  error = signal<string | null>(null);

  constructor() {
    effect(() => {
      void this.selfForm.valueChanges.subscribe(() => this.clearInfo());
      void this.usernameForm.valueChanges.subscribe(() => this.clearInfo());
      void this.passwordForm.valueChanges.subscribe(() => this.clearInfo());
    });
  }

  // ---- Self-delete handler ----
  async onDeleteMe() {
    this.clearInfo();
    if (this.selfForm.invalid) {
      this.error.set('Please type the confirmation phrase exactly.');
      return;
    }
    const typed = (this.selfForm.value.confirmPhrase || '').trim().toLowerCase();
    if (typed !== this.confirmPhraseRequired) {
      this.error.set(`Confirmation must be exactly: “${this.confirmPhraseRequired}”.`);
      return;
    }

    this.busy.set(true);
    try {
      await this.users.deleteMeAsync();
      this.message.set('Your account was deleted. Signing you out…');
      this.logoutAndGoLogin(800);
    } catch (e: any) {
      this.error.set(this.humanizeError(e));
    } finally {
      this.busy.set(false);
    }
  }

  // ---- Change username ----
  async onChangeUsername() {
    this.clearInfo();
    if (this.usernameForm.invalid) {
      this.error.set('Fill out current password and a valid new username.');
      return;
    }
    const { currentPassword, newUsername } = this.usernameForm.value as any;

    this.busy.set(true);
    try {
      await this.users.updateMyUsernameAsync(currentPassword, newUsername);
      this.message.set('Username updated successfully.');
      // Keep session; update cached username if you store it
      try { localStorage.setItem('username', newUsername); } catch {}
      this.usernameForm.reset();
    } catch (e: any) {
      this.error.set(this.humanizeError(e));
    } finally {
      this.busy.set(false);
    }
  }

  // ---- Change password ----
  async onChangePassword() {
    this.clearInfo();
    if (this.passwordForm.invalid) {
      if (this.passwordForm.errors?.['mismatch']) {
        this.error.set('Passwords do not match.');
      } else {
        this.error.set('Fill out current password and a new password (min 8 chars).');
      }
      return;
    }
    const { currentPassword, newPassword } = this.passwordForm.value as any;

    this.busy.set(true);
    try {
      await this.users.updateMyPasswordAsync(currentPassword, newPassword);
      this.message.set('Password updated. Please sign in again.');
      this.logoutAndGoLogin(800);
    } catch (e: any) {
      this.error.set(this.humanizeError(e));
    } finally {
      this.busy.set(false);
    }
  }

  // ---- helpers ----
  private logoutAndGoLogin(delayMs = 0) {
    try {
      // If you have an AuthService.logout(), call it here instead.
      localStorage.removeItem('jwt');
      localStorage.removeItem('token');
      localStorage.removeItem('roles');
      // keep username cleared after password change
      localStorage.removeItem('username');
    } catch {}
    setTimeout(() => this.router.navigateByUrl('/login'), delayMs);
  }

  private clearInfo() { this.message.set(null); this.error.set(null); }

  private humanizeError(e: any): string {
    if (!e) return 'Request failed.';
    if (e.status === 400) return 'Invalid input.';
    if (e.status === 401) return 'Current password is incorrect or session expired.';
    if (e.status === 403) return 'Forbidden.';
    if (e.status === 409) return 'Username already in use.';
    if (e.status === 0) return 'Network error. Is the backend running?';
    try {
      const body = e.error;
      if (body && typeof body === 'object') {
        if (body.message) return `Error: ${body.message}`;
        if (body.error) return `Error: ${body.error}`;
      }
    } catch {}
    return `Error ${e.status || ''}`.trim();
  }
}
