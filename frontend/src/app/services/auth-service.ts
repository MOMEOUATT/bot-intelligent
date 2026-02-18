import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../models/user';
import { Login } from '../components/login/login';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User |null>;

  constructor() {
    const storedUser = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<User | null>(
      storedUser ? JSON.parse(storedUser) : null
    )
    this.currentUser = this.currentUserSubject.asObservable();
  }

  /**
   * Charger l'utilisateur depuis le localStorage
   */
  private loadUser(): User | null {
    try {
      const stored = localStorage.getItem('currentUser');
      if (!stored || stored === 'null' || stored === 'undefined') {
        return null;
      }
      return JSON.parse(stored);
    } catch (error) {
      console.error('Erreur chargement user:', error);
      localStorage.removeItem('currentUser');
      return null;
    }
  }

  /**
   * Obtenir l'utilisateur actuel - TOUJOURS depuis localStorage
   */
  public get currentUserValue(): User | null {
    return this.loadUser();
  }

  /**
   * Connexion
   */
  login(user: User): void {
    localStorage.setItem('currentUser', JSON.stringify(user));
    this.currentUserSubject.next(user);
    console.log('Login effectué:', user);
  }

  /**
   * Déconnexion
   */
  logout(): void {
    // 1. Effacer le localStorage
    localStorage.removeItem('currentUser');
    localStorage.clear();
    
    // 2. Mettre à jour le BehaviorSubject
    this.currentUserSubject.next(null);
    
    // 3. Vérification
    const check = this.loadUser();
    console.log('Déconnexion effectuée, localStorage:', localStorage.getItem('currentUser'));
    console.log('Vérification user après logout:', check);
  }

  /**
   * Vérifier si connecté - LECTURE DIRECTE du localStorage
   */
  isLoggedIn(): boolean {
    const user = this.loadUser();
    const isLogged = user !== null && user !== undefined;
    console.log('isLoggedIn check - user:', user, 'result:', isLogged);
    return isLogged;
  }

  updateCurrentUser(user: User): void {
    localStorage.setItem("currentUser", JSON.stringify(user));
    this.currentUserSubject.next(user);
  }
}
