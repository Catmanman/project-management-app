import { HttpInterceptorFn } from '@angular/common/http';

/**
 * Interceptor that attaches the JWT bearer token to outgoing HTTP
 * requests.  Tokens are retrieved from localStorage.  If no token is
 * available the request proceeds unmodified.
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  if (token) {
    return next(req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
  }
  return next(req);
};