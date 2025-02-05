import { Component, EventEmitter, Output } from '@angular/core';
import { BookService } from '../../services/book.service';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css'],
})
export class SearchBarComponent {
  @Output() searchResults: EventEmitter<any[]> = new EventEmitter(); // Emitir resultados dinámicos
  @Output() searchQueryChange: EventEmitter<string> = new EventEmitter(); // Emitir búsqueda completa o vacía
  searchQuery: string = ''; // Texto del input
  previewBooks: any[] = []; // Resultados dinámicos para la vista previa
  private searchSubject: Subject<string> = new Subject(); // Subject para el debounce

  constructor(private bookService: BookService) {}

  ngOnInit() {
    // Configurar el debounce
    this.searchSubject
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((query) => {
        if (query.trim()) {
          this.bookService.searchBooks(query).subscribe((data) => {
            this.previewBooks = data.items || [];
            this.searchResults.emit(this.previewBooks); // Emitir resultados dinámicos
          });
        } else {
          this.previewBooks = [];
        }
      });
  }

  onSearchKeyUp(): void {
    // Emitir el texto actual del input al Subject
    this.searchSubject.next(this.searchQuery);
  }

  onSearchEnter(): void {
    // Emitir el texto de búsqueda completa o vacío
    this.searchQueryChange.emit(this.searchQuery.trim());
    this.previewBooks = []; // Limpiar la vista previa
  }

  selectPreviewBook(book: any): void {
    // Emitir el libro seleccionado al padre
    this.searchQuery = book.volumeInfo.title;
    this.searchQueryChange.emit(this.searchQuery);
    this.previewBooks = [];
  }
}
