import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';


@Injectable({
  providedIn: 'root',
})
export class NotificationService {

  constructor(private snackBar: MatSnackBar) {}

  // Toast de succès (vert)
  success(message: string, duration: number = 3000): void {
    this.show(message, 'success-toast', duration);
  }

  // Toast d'erreur (rouge)
  error(message: string, duration: number = 4000): void {
    this.show(message, 'error-toast', duration);
  }

  // Toast d'info (bleu)
  info(message: string, duration: number = 3000): void {
    this.show(message, 'info-toast', duration);
  }

  // Toast d'avertissement (orange)
  warning(message: string, duration: number = 3000): void {
    this.show(message, 'warning-toast', duration);
  }

  private show(message: string, panelClass: string, duration: number): void {
    const config: MatSnackBarConfig = {
      duration,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: [panelClass, 'custom-snackbar']
    };

    this.snackBar.open(message, '✕', config);
  }
  
}
