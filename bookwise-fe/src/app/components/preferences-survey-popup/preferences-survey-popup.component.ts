import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-preferences-survey-popup',
  templateUrl: './preferences-survey-popup.component.html',
  styleUrls: ['./preferences-survey-popup.component.css'],
})
export class PreferencesSurveyPopupComponent {
  @Output() close = new EventEmitter<void>();
  @Output() continue = new EventEmitter<void>();

  closePopup() {
    this.close.emit();
  }

  proceed() {
    this.continue.emit();
  }
}
