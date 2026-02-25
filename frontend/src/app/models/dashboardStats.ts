export interface DashboardStats {
  totalConversations: number;
  totalMessages: number;
  likedMessages: number;
  dislikedMessages: number;
  dailyStats: DailyStats[];
}

export interface DailyStats {
  date: string;
  messages: number;
}