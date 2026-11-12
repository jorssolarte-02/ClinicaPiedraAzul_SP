import { HttpInterceptorFn } from '@angular/common/http';

const PUBLIC_PATHS = ['/auth/login', '/doctors/public', '/patients/public', '/patients/exists/'];
export const jwtInterceptor: HttpInterceptorFn = (request, next) => {
  const isApi = request.url.includes('/api/');
  const isPublic = PUBLIC_PATHS.some(path => request.url.includes(path));
  const token = localStorage.getItem('clinic.token');
  return isApi && !isPublic && token
    ? next(request.clone({ setHeaders: { Authorization: `Bearer ${token}` } }))
    : next(request);
};
