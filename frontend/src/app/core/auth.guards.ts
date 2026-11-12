import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService, } from './auth.service';
import { Role } from '../models/clinic.models';

export const authenticationGuard: CanActivateFn = (_route, state) => {
  const auth = inject(AuthService); const router = inject(Router);
  return auth.isAuthenticated() ? true : router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};
export const roleGuard: CanActivateFn = route => {
  const auth = inject(AuthService); const router = inject(Router); const expected = route.data['role'] as Role | undefined;
  if (!auth.isAuthenticated()) return router.createUrlTree(['/login']);
  if (expected && auth.role() !== expected) return router.createUrlTree(['/forbidden']);
  return true;
};
