import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type Theme = 'dark' | 'light';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  private readonly THEME_KEY = 'lumin-theme';
  private themeSubject = new BehaviorSubject<Theme>(this.getStoredTheme());

  theme$ = this.themeSubject.asObservable();

  constructor() {
    // Appliquer le thème au démarrage
    this.applyTheme(this.themeSubject.value);
  }

  // Récupérer le thème stocké (ou dark par défaut)
  private getStoredTheme(): Theme {
    const stored = localStorage.getItem(this.THEME_KEY) as Theme;
    return stored || 'dark';
  }

  // Obtenir le thème actuel
  getCurrentTheme(): Theme {
    return this.themeSubject.value;
  }

  // Basculer entre dark et light
  toggleTheme(): void {
    const newTheme: Theme = this.themeSubject.value === 'dark' ? 'light' : 'dark';
    this.setTheme(newTheme);
  }

  // Définir un thème spécifique
  setTheme(theme: Theme): void {
    this.themeSubject.next(theme);
    localStorage.setItem(this.THEME_KEY, theme);
    this.applyTheme(theme);
  }

  // Appliquer le thème au DOM
  private applyTheme(theme: Theme): void {
    const body = document.body;
    
    // Retirer les anciennes classes
    body.classList.remove('dark-theme', 'light-theme');
    
    // Ajouter la nouvelle classe
    body.classList.add(`${theme}-theme`);
    
    console.log('Thème appliqué:', theme);
  }

  // Vérifier si le thème actuel est sombre
  isDarkTheme(): boolean {
    return this.themeSubject.value === 'dark';
  }
}