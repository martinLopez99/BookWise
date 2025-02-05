import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.css'],
})
export class BookCardComponent {
  @Input() book: any;
  @Output() favoriteAdded = new EventEmitter<any>();

  addToFavorites(): void {
    this.favoriteAdded.emit(this.book);
  }
}
