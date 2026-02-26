import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';

import { StatsService } from '../../services/stats-service';
import { AuthService } from '../../services/auth-service';
import { ApiService } from '../../services/api-service';
import { DashboardStats } from '../../models/dashboardStats';
import { Conversation } from '../../models/conversation';

import { StatCardComponent } from './widgets/stat-card/stat-card';
import { MessagesChartComponent } from './widgets/messages-chart/messages-chart';
import { FeedbackChartComponent } from './widgets/feedback-chart/feedback-chart';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatCardModule,
    StatCardComponent,
    MessagesChartComponent,
    FeedbackChartComponent
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})

export class DashboardComponent implements OnInit {

  loading = true;
  stats?: DashboardStats;
  recentConversations: Conversation[] = [];
  selectedDays = 7; // ✅ Période par défaut
  availablePeriods = [
    { value: 7, label: '7 jours' },
    { value: 14, label: '14 jours' },
    { value: 30, label: '30 jours' },
    { value: 90, label: '90 jours' }
  ];

  constructor(
    private statsService: StatsService,
    private authService: AuthService,
    private apiService: ApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  // ✅ Méthode modifiée pour accepter la période
  loadDashboard(): void {
    const user = this.authService.currentUserValue;
    if (!user) {
      this.router.navigate(['/auth']);
      return;
    }

    this.loading = true;

    // Charger les stats avec la période sélectionnée
    this.statsService.getDashboard(user.id, this.selectedDays).subscribe({
      next: (stats) => {
        this.stats = stats;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur chargement stats:', err);
        this.loading = false;
      }
    });

    // Charger les conversations récentes (inchangé)
    this.apiService.getUserConversation(user.id).subscribe({
      next: (conversations) => {
        this.recentConversations = conversations.slice(0, 5);
      },
      error: (err) => {
        console.error('Erreur chargement conversations:', err);
      }
    });
  }

  // ✅ Nouvelle méthode pour changer la période
  onPeriodChange(days: number): void {
    this.selectedDays = days;
    this.loadDashboard();
  }

  goToChat(): void {
    this.router.navigate(['/chat']);
  }

  goToConversation(id: number): void {
    this.router.navigate(['/chat', id]);
  }

  formatDate(date: Date): string {
    const d = new Date(date);
    return d.toLocaleDateString('fr-FR', { 
      day: 'numeric', 
      month: 'short',
      year: 'numeric'
    });
  }
}