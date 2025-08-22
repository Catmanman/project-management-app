import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/**
 * Registration form component.  After a successful registration, the
 * newly created user is automatically logged in and redirected to
 * the projects page.
 */
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  error: string | null = null;

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.loading = true;
    this.error = null;
    const { username, password } = this.form.value;
    this.auth.register(username, password).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/projects']);
      },
      error: (err) => {
        this.loading = false;
        if (err.status === 409) {
          this.error = 'Username is already taken';
        } else {
          this.error = 'Registration failed';
        }
      }
    });
  }
}