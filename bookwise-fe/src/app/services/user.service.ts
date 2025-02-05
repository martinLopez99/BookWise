import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private preferences: { gender: string[]; topics: string[] } | null = null;

  constructor() {}

  // Método para establecer las preferencias
  setPreferences(preferences: { gender: string[]; topics: string[] }): void {
    this.preferences = preferences;
  }

  // Método para obtener las preferencias
  getPreferences(): { gender: string[]; topics: string[] } {
    const preferredGenre = localStorage.getItem('userPreferredGenre');
    return {
      gender: preferredGenre ? [preferredGenre] : [],
      topics: this.preferences?.topics || [],
    };
  }
}

