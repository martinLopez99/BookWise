import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { BookService } from '../../services/book.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css'],
})
export class WishlistComponent implements OnInit {
  wishlist: any[] = []; // Lista de libros con detalles de Google Books

  constructor(private authService: AuthService, private http: HttpClient, private bookService: BookService) {}

  ngOnInit(): void {
    const email = localStorage.getItem('userEmail');
    if (email) {
      this.authService.getWishlist(email).subscribe({
        next: (bookIds: string[]) => {
          // Consulta la API de Google Books para cada ID
          bookIds.forEach((id) => {
            this.http
              .get(`https://www.googleapis.com/books/v1/volumes/${id}`)
              .subscribe((book) => {
                this.wishlist.push(book);
              });
          });
        },
        error: (err) => {
          console.error('Error al obtener la lista de deseados:', err);
        },
      });
    }
  }

  removeFromWishlist(bookId: string): void {
    const email = localStorage.getItem('userEmail');
    if (email) {
      this.bookService.removeFromWishlist(email, bookId).subscribe({
        next: () => {
          this.wishlist = this.wishlist.filter((book) => book.id !== bookId);
        },
        error: (err) => {
          console.error('Error al eliminar el libro de la lista de deseados:', err);
        },
      });
    }
  }

  markAsRead(bookId: string): void {
    const email = localStorage.getItem('userEmail');
    if (email) {
      this.bookService.addToReadingHistory(email, bookId).subscribe({
        next: () => {
          this.removeFromWishlist(bookId);
        },
        error: (err) => {
          console.error('Error al marcar el libro como leído:', err);
        },
      });
    }
  }
}
