import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-stat-card',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  template: `
    <mat-card class="stat-card">
      <div class="stat-icon" [style.background]="iconBackground">
        <mat-icon>{{ icon }}</mat-icon>
      </div>
      <div class="stat-content">
        <h3 class="stat-value">{{ value }}</h3>
        <p class="stat-label">{{ label }}</p>
      </div>
    </mat-card>
  `,
  styles: [`
    .stat-card {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 20px;
      background: rgba(26, 31, 46, 0.8) !important;
      border: 1px solid rgba(167, 139, 250, 0.15);
      border-radius: 12px;
      transition: all 0.3s ease;
    }

    .stat-card:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 24px rgba(139, 92, 246, 0.2);
    }

    .stat-icon {
      width: 56px;
      height: 56px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .stat-icon mat-icon {
      font-size: 28px;
      width: 28px;
      height: 28px;
      color: white;
    }

    .stat-content {
      flex: 1;
    }

    .stat-value {
      font-size: 32px;
      font-weight: 700;
      color: #f1f5f9;
      margin: 0 0 4px;
      line-height: 1;
    }

    .stat-label {
      font-size: 14px;
      color: #94a3b8;
      margin: 0;
    }
  `]
})
export class StatCardComponent {
  @Input() icon: string = 'info';
  @Input() value: number | string = 0;
  @Input() label: string = '';
  @Input() iconBackground: string = 'linear-gradient(135deg, #8b5cf6, #a78bfa)';
}