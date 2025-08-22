import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { jwtInterceptor } from './services/jwt.interceptor';

/**
 * Application-level configuration.  The router and HTTP client are
 * configured here along with the JWT interceptor which adds the
 * Authorization header to each request when a token is present.
 */
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([jwtInterceptor]))
  ]
};