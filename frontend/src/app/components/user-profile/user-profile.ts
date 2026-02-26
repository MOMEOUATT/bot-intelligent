import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { User } from '../../models/user';
import { ApiService } from '../../services/api-service';
import { AuthService } from '../../services/auth-service';
import { NotificationService } from '../../services/notification-service';
import { MatCardModule } from '@angular/material/card';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ChangePasswordDialog } from '../change-password-dialog/change-password-dialog';

@Component({
  selector: 'app-user-profile',
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule
  ],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css',
})
export class UserProfile implements OnInit {

  user: User | null = null;
  username = '';
  email = '';
  loading = false;

  private originalUsername = '';
  private originalEmail = '';

  constructor(
    private apiService: ApiService,
    private authService: AuthService,
    private notification: NotificationService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.authService.currentUserValue;
    if (this.user) {
      this.username = this.user.username;
      this.email = this.user.email;
      this.originalUsername = this.user.username;
      this.originalEmail = this.user.email;
    } else {
      this.router.navigate(['/auth']);
    }
  }

  getInitials(): string {
    if (!this.username) return 'U';
    const names = this.username.split(' ');
    if (names.length >= 2) {
      return (names[0][0] + names[1][0]).toUpperCase();
    }
    return this.username.substring(0, 2).toUpperCase();
  }

  hasChanges(): boolean {
    return this.username !== this.originalUsername || 
           this.email !== this.originalEmail;
  }

  onSave(): void {
    if (!this.user || this.loading) return;

    this.loading = true;

    this.apiService.updateUserProfile(this.user.id, this.username, this.email).subscribe({
      next: (updated) => {
        this.authService.updateCurrentUser(updated);
        this.originalUsername = updated.username;
        this.originalEmail = updated.email;
        this.notification.success('Profil mis à jour');
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur:', err);
        const message = err.error?.message || 'Impossible de mettre à jour le profil';
        this.notification.error(message);
        this.loading = false;
      }
    });
  }

  onChangePassword(): void {
    const dialogRef = this.dialog.open(ChangePasswordDialog, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && this.user) {
        this.apiService.changePassword(
          this.user.id, 
          result.oldPassword, 
          result.newPassword
        ).subscribe({
          next: () => {
            this.notification.success('Mot de passe changé avec succès');
          },
          error: (err) => {
            console.error('Erreur:', err);
            this.notification.error('Mot de passe actuel incorrect');
          }
        });
      }
    });
  }

  onCancel(): void {
    this.username = this.originalUsername;
    this.email = this.originalEmail;
  }

  goBack(): void {
    this.router.navigate(['/chat']);
  }
}
