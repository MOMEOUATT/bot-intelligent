import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration } from 'chart.js';
import { DailyStats } from '../../../../models/dashboardStats';

@Component({
  selector: 'app-messages-chart',
  standalone: true,
  imports: [CommonModule, MatCardModule, BaseChartDirective],
  templateUrl: './messages-chart.html',
  styleUrl: './messages-chart.css'
})
export class MessagesChartComponent implements OnInit {
  @Input() dailyStats: DailyStats[] = [];
  @Input() subtitle: string = '7 derniers jours';

  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  chartData: ChartConfiguration['data'] = {
    labels: [],
    datasets: []
  };

  chartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false
      }
    },
    scales: {
      x: {
        grid: {
          color: 'rgba(167, 139, 250, 0.1)'
        },
        ticks: {
          color: '#94a3b8'
        }
      },
      y: {
        beginAtZero: true,
        grid: {
          color: 'rgba(167, 139, 250, 0.1)'
        },
        ticks: {
          color: '#94a3b8',
          precision: 0
        }
      }
    }
  };

  ngOnInit(): void {
    this.updateChart();
  }

  updateChart(): void {
    const labels = this.dailyStats.map(d => {
      const date = new Date(d.date);
      return date.toLocaleDateString('fr-FR', { day: 'numeric', month: 'short' });
    });

    // console.log("daylyStats: ", this.dailyStats);

    const data = this.dailyStats.map(d => d.messages);
    // console.log("data: ", data);

    this.chartData = {
      labels: labels,
      datasets: [{
        label: 'Messages',
        data: data,
        borderColor: '#a78bfa',
        backgroundColor: 'rgba(167, 139, 250, 0.1)',
        fill: true,
        tension: 0.4,
        pointBackgroundColor: '#a78bfa',
        pointBorderColor: '#fff',
        pointBorderWidth: 2,
        pointRadius: 4,
        pointHoverRadius: 6
      }]
    };

    this.chart?.update();
  }
}