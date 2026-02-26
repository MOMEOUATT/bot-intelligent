import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';

/**
 * Guard pour les routes qui nécessitent une authentification
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  console.log('authGuard - isLoggedIn:', authService.isLoggedIn());

  if (authService.isLoggedIn()) {
    return true;
  }

  console.log('authGuard - Redirection vers /auth');
  router.navigate(['/auth']);
  return false;
};

/**
 * Guard pour les routes accessibles seulement si NON connecté (login, signup)
 */
export const noAuthGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  console.log('noAuthGuard - isLoggedIn:', authService.isLoggedIn());

  if (!authService.isLoggedIn()) {
    return true;
  }

  console.log('noAuthGuard - Redirection vers /chat');
  router.navigate(['/chat']);
  return false;
};