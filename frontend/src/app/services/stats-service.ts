import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardStats } from '../models/dashboardStats';

@Injectable({
  providedIn: 'root'
})
export class StatsService {

  private apiUrl = 'http://localhost:8080/api/stats';

  constructor(private http: HttpClient) {}

  // Dashboard complet
  getDashboard(userId: number, days: number = 7): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.apiUrl}/user/${userId}/dashboard?days=${days}`);
  }

  // Stats individuelles (si besoin plus tard)
  getUserMessagesCount(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/user/${userId}/messages/count`);
  }

  getLikedMessagesCount(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/user/${userId}/messages/liked/count`);
  }

  getDislikedMessagesCount(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/user/${userId}/messages/disliked/count`);
  }
}