import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-feedback-chart',
  standalone: true,
  imports: [CommonModule, MatCardModule, BaseChartDirective],
  template: `
    <mat-card class="chart-card">
      <mat-card-header>
        <h3>Répartition feedback</h3>
        <p>Satisfaction globale</p>
      </mat-card-header>
      <mat-card-content>
        <canvas 
          baseChart
          [data]="chartData"
          [options]="chartOptions"
          [type]="'doughnut'">
        </canvas>
        <div class="legend">
          <div class="legend-item">
            <span class="dot liked"></span>
            <span>Utiles: {{ likedMessages }} ({{ likedPercent }}%)</span>
          </div>
          <div class="legend-item">
            <span class="dot disliked"></span>
            <span>Pas utiles: {{ dislikedMessages }} ({{ dislikedPercent }}%)</span>
          </div>
          <div class="legend-item">
            <span class="dot neutral"></span>
            <span>Sans avis: {{ neutralMessages }} ({{ neutralPercent }}%)</span>
          </div>
        </div>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    .chart-card {
      background: rgba(26, 31, 46, 0.8) !important;
      border: 1px solid rgba(167, 139, 250, 0.15);
      border-radius: 12px;
    }

    mat-card-header {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      padding: 20px 20px 0;
    }

    mat-card-header h3 {
      font-size: 18px;
      font-weight: 600;
      color: #f1f5f9;
      margin: 0 0 4px;
    }

    mat-card-header p {
      font-size: 13px;
      color: #64748b;
      margin: 0;
    }

    mat-card-content {
      padding: 20px;
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    canvas {
      max-width: 250px;
      max-height: 250px;
      margin-bottom: 20px;
    }

    .legend {
      width: 100%;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    .legend-item {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 14px;
      color: #cbd5e1;
    }

    .dot {
      width: 12px;
      height: 12px;
      border-radius: 50%;
    }

    .dot.liked {
      background: #10b981;
    }

    .dot.disliked {
      background: #ef4444;
    }

    .dot.neutral {
      background: #64748b;
    }
  `]
})
export class FeedbackChartComponent implements OnInit {
  @Input() totalMessages: number = 0;
  @Input() likedMessages: number = 0;
  @Input() dislikedMessages: number = 0;

  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  neutralMessages = 0;
  likedPercent = 0;
  dislikedPercent = 0;
  neutralPercent = 0;

  chartData: ChartConfiguration['data'] = {
    labels: ['Utiles', 'Pas utiles', 'Sans avis'],
    datasets: []
  };

  chartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false
      }
    }
  };

  ngOnInit(): void {
    this.calculateStats();
    this.updateChart();
  }

  calculateStats(): void {
    this.neutralMessages = this.totalMessages - (this.likedMessages + this.dislikedMessages);
    
    if (this.totalMessages > 0) {
      this.likedPercent = Math.round((this.likedMessages / this.totalMessages) * 100);
      this.dislikedPercent = Math.round((this.dislikedMessages / this.totalMessages) * 100);
      this.neutralPercent = Math.round((this.neutralMessages / this.totalMessages) * 100);
    }
  }

  updateChart(): void {
    this.chartData = {
      labels: ['Utiles', 'Pas utiles', 'Sans avis'],
      datasets: [{
        data: [this.likedMessages, this.dislikedMessages, this.neutralMessages],
        backgroundColor: ['#10b981', '#ef4444', '#64748b'],
        borderWidth: 0
      }]
    };

    this.chart?.update();
  }
}