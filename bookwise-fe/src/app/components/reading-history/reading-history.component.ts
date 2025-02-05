import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book.service';

@Component({
  selector: 'app-reading-history',
  templateUrl: './reading-history.component.html',
  styleUrls: ['./reading-history.component.css'],
})
export class ReadingHistoryComponent implements OnInit {
  readingHistory: any[] = []; // Lista de libros con detalles de Google Books

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    const email = localStorage.getItem('userEmail');
    if (email) {
      this.bookService.getReadingHistory(email).subscribe({
        next: (bookIds: string[]) => {
          bookIds.forEach((id) => {
            this.bookService.getBookDetails(id).subscribe((book) => {
              this.readingHistory.push({
                volumeInfo: book.volumeInfo,
                finishedDate: new Date(), // Opcional: Puedes ajustar esto según tus datos
              });
            });
          });
        },
        error: (err) => {
          console.error('Error al obtener el historial de lectura:', err);
        },
      });
    }
  }
}
