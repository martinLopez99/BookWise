import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-favorite-popup',
  templateUrl: './favorite-popup.component.html',
  styleUrls: ['./favorite-popup.component.css']
})
export class FavoritePopupComponent {
  @Input() show: boolean = false;
  @Input() message: string = '';
  @Output() popupClosed = new EventEmitter<void>();
  @Output() loginRedirect = new EventEmitter<void>();

  constructor(private router: Router) {}

  closePopup(): void {
    this.show = false;
    this.popupClosed.emit();
  }

  redirectToLogin(): void {
    this.show = false;
    this.router.navigate(['/auth']);
  }
}
