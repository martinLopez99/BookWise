
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.getAuthStateFromStorage());
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private loginUrl = 'http://localhost:8080/book-wise/api/user/login';
  private registerUrl = 'http://localhost:8080/book-wise/api/user/register';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return new Observable((observer) => {
      this.http.post(this.loginUrl, { email, password }).subscribe({
        next: (response: any) => {
          console.log('Login exitoso:', response);
          localStorage.setItem('token', response.token);
          this.setAuthState(true);
          observer.next(response);
          observer.complete();
        },
        error: (err) => {
          console.error('Error al iniciar sesión:', err);
          this.setAuthState(false);
          observer.error(err);
        },
      });
    });
  }

  register(username: string, email: string, password: string): Observable<any> {
    return this.http.post(this.registerUrl, { username, email, password });
  }

  getUserInfo(email: string): Observable<any> {
    const url = `http://localhost:8080/book-wise/api/user/info?email=${email}`;
    return this.http.get(url);
  }

  getWishlist(email: string): Observable<string[]> {
    const url = `http://localhost:8080/book-wise/api/user/wishlist?email=${email}`;
    return this.http.get<string[]>(url);
  }

  submitSurvey(email: string, responses: any[]): Observable<void> {
    const url = `http://localhost:8080/book-wise/api/user/survey?email=${email}`;
    return this.http.post<void>(url, responses);
  }


  logout() {
    this.setAuthState(false);
  }

  isLoggedIn(): boolean {
    return this.isAuthenticatedSubject.value;
  }

  private setAuthState(state: boolean) {
    this.isAuthenticatedSubject.next(state);
    localStorage.setItem('isAuthenticated', state.toString());
  }

  private getAuthStateFromStorage(): boolean {
    return localStorage.getItem('isAuthenticated') === 'true';
  }
}
