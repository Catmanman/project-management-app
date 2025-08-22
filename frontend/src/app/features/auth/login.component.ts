import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/**
 * Login form component.  Collects user credentials and invokes the
 * AuthService to authenticate.  Successful authentication results
 * in a redirect either to a stored returnUrl or the projects page.
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

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
    this.auth.login(username, password).subscribe({
      next: () => {
        this.loading = false;
        // Redirect to returnUrl if present, otherwise default
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/projects';
        this.router.navigateByUrl(returnUrl);
      },
      error: (err) => {
        this.loading = false;
        this.error = 'Invalid credentials';
      }
    });
  }
}