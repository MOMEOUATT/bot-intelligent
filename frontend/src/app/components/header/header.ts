import { Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../services/auth-service';
import { User } from '../../models/user';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { WaveformLogo } from '../waveform-logo/waveform-logo';

@Component({
  selector: 'app-header',
  imports: [
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatDividerModule,
    CommonModule,
    WaveformLogo
  ],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit {

  currentUser: User | null = null;
  
  constructor(
    private authService: AuthService, 
    private router: Router
  ){}

  ngOnInit(): void {
      this.authService.currentUser.subscribe(
        user => this.currentUser = user
      );
  }

  getUserName(): string {
    return this.currentUser?.username || "Utilisateur";
  }

  getUserEmail(): string {
    return this.currentUser?.email || "";
  }

  getUserInitials(): string {
    if (!this.currentUser?.username) return 'U';
    const names = this.currentUser.username.split(' ');
    if (names.length >= 2) {
      return (names[0][0] + names[1][0]).toUpperCase();
    }
    return this.currentUser.username.substring(0, 2).toUpperCase();
  }

  onProfile(): void {
    // TODO: Implémenter la page profil
    console.log("Profil");
  }

  onSettings(): void {
    // TODO: Implémenter les paramètres
    console.log("Paramètres");
  }

  onLogout(): void {
    console.log('Logout clicked');
    this.authService.logout();
    setTimeout(() => {
      window.location.href = '/auth';
    }, 100);
  }
}
