import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-feedback-chart',
  standalone: true,
  imports: [CommonModule, MatCardModule, BaseChartDirective],
  templateUrl: './feedback-chart.html',
  styleUrl: './feedback-chart.css'
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