import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-change-password-dialog',
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './change-password-dialog.html',
  styleUrl: './change-password-dialog.css',
})
export class ChangePasswordDialog {

  oldPassword = '';
  newPassword = '';
  confirmPassword = '';
  showError = false;
  errorMessage = '';

  hideOld = true;
  hideNew = true;
  hideConfirm = true;

  constructor(
    public dialogRef: MatDialogRef<ChangePasswordDialog>
  ) {}

  isValid(): boolean {
    return this.oldPassword.length > 0 &&
           this.newPassword.length >= 6 &&
           this.confirmPassword.length > 0;
  }

  getStrengthClass(): string {
    if (this.newPassword.length < 6) return 'weak';
    if (this.newPassword.length < 10) return 'medium';
    return 'strong';
  }

  getStrengthText(): string {
    if (this.newPassword.length < 6) return 'Faible';
    if (this.newPassword.length < 10) return 'Moyen';
    return 'Fort';
  }

  onConfirm(): void {
    if (!this.isValid()) {
      this.showError = true;
      this.errorMessage = 'Veuillez remplir tous les champs correctement';
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.showError = true;
      this.errorMessage = 'Les mots de passe ne correspondent pas';
      return;
    }

    if (this.oldPassword === this.newPassword) {
      this.showError = true;
      this.errorMessage = 'Le nouveau mot de passe doit être différent de l\'ancien';
      return;
    }

    this.dialogRef.close({
      oldPassword: this.oldPassword,
      newPassword: this.newPassword
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

}
