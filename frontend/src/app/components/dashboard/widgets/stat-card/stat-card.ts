import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-stat-card',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  templateUrl: './stat-card.html',
  styleUrl: './stat-card.css'
})
export class StatCardComponent {
  @Input() icon: string = 'info';
  @Input() value: number | string = 0;
  @Input() label: string = '';
  @Input() iconBackground: string = 'linear-gradient(135deg, #8b5cf6, #a78bfa)';
}