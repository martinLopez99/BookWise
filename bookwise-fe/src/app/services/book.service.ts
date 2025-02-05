import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private googleBooksApiUrl = 'https://www.googleapis.com/books/v1/volumes';
  private wishlistApiUrl = 'http://localhost:8080/book-wise/api/user/wishlist';
  private readingHistoryApiUrl = 'http://localhost:8080/book-wise/api/user/reading-history';

  constructor(private http: HttpClient) {}

  // Método para buscar libros en la API de Google Books
  searchBooks(query: string, startIndex: number = 0, maxResults: number = 10): Observable<any> {
    const url = `${this.googleBooksApiUrl}?q=${query}&startIndex=${startIndex}&maxResults=${maxResults}`;
    return this.http.get(url);
  }

  // Método para agregar libros a la lista de favoritos en el backend
  addToWishlist(email: string, bookId: string): Observable<void> {
    const url = `${this.wishlistApiUrl}?email=${email}&bookId=${bookId}`;
    return this.http.post<void>(url, {});
  }

  // Método para obtener detalles de un libro por ID
  getBookDetails(bookId: string): Observable<any> {
    const url = `${this.googleBooksApiUrl}/${bookId}`;
    return this.http.get(url);
  }

  // Método para eliminar un libro de la lista de deseados
  removeFromWishlist(email: string, bookId: string): Observable<void> {
    const url = `${this.wishlistApiUrl}?email=${email}&bookId=${bookId}`;
    return this.http.delete<void>(url);
  }

  // Método para agregar un libro al historial de lectura
  addToReadingHistory(email: string, bookId: string): Observable<void> {
    const url = `${this.readingHistoryApiUrl}?email=${email}&bookId=${bookId}`;
    return this.http.post<void>(url, {});
  }

  getReadingHistory(email: string): Observable<string[]> {
    const url = `http://localhost:8080/book-wise/api/user/reading-history?email=${email}`;
    return this.http.get<string[]>(url);
  }

}
