import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})

export class HomeComponent implements OnInit {
  books: any[] = [];
  query: string = 'Bestsellers';
  currentPage: number = 0;
  maxResults: number = 10;
  totalItems: number = 0;
  hasNextPage: boolean = false;
  visiblePages: number = 5;
  popupMessage: string = '';
  showPopup: boolean = false;

  constructor(private bookService: BookService, private userService: UserService) {}

  ngOnInit() {
    this.checkLoginStatus();
  }

  loadBooksBasedOnPreferences(): void {
    const preferences = this.userService.getPreferences();

    if (preferences && preferences.gender && preferences.gender.length > 0) {
      this.query = `${preferences.gender[0]} bestsellers`; // Filtra por género preferido
    } else {
      this.loadDefaultBooks();
    }

    this.loadBooks();
  }

  loadBooks(): void {
    const startIndex = this.currentPage * this.maxResults;
    this.bookService.searchBooks(this.query, startIndex, this.maxResults).subscribe((data) => {
      this.books = data.items || [];
      this.totalItems = data.totalItems || 0;
      this.hasNextPage = startIndex + this.maxResults < this.totalItems;
    });
  }

  nextPage(): void {
    if (this.hasNextPage) {
      this.currentPage++;
      this.loadBooks();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadBooks();
    }
  }

  searchBooks(query: string): void {
    this.query = query;
    this.currentPage = 0;
    this.loadBooks();
  }


  goToPage(page: number): void {
    this.currentPage = page;
    this.loadBooks();
  }

  getPageNumbers(): number[] {
    const totalPages = Math.ceil(this.totalItems / this.maxResults);
    const startPage = Math.max(0, this.currentPage - Math.floor(this.visiblePages / 2));
    const endPage = Math.min(totalPages, startPage + this.visiblePages);

    return Array.from({ length: endPage - startPage }, (_, i) => startPage + i);
  }

  handleFavoriteAdded(book: any): void {
    const email = localStorage.getItem('userEmail');
    if (email) {
      this.bookService.addToWishlist(email, book.id).subscribe({
        next: () => {
          this.popupMessage = `¡Libro "${book.volumeInfo.title}" agregado a favoritos!`;
          this.showPopup = true;
        },
        error: (err) => {
          console.error('Error al agregar el libro a favoritos:', err);
          this.popupMessage = 'Hubo un error al agregar el libro a favoritos. Inténtalo nuevamente.';
          this.showPopup = true;
        },
      });
    } else {
      // Mostrar mensaje para iniciar sesión
      this.popupMessage = 'Debes iniciar sesión para agregar libros a favoritos.';
      this.showPopup = true;
    }
  }

  checkLoginStatus(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.loadBooksBasedOnPreferences();
    } else {
      this.loadDefaultBooks();
    }
  }

  loadDefaultBooks(): void {
    this.query = 'Bestsellers';
    this.loadBooks();
  }

  closePopup(): void {
    this.showPopup = false;
  }

  redirectToLogin(): void {
    this.showPopup = false;
    window.location.href = '/auth';
  }


}
