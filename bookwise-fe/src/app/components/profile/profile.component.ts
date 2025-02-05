import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  user: any = {
    name: '',
    email: '',
  };

  isPopupVisible = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    const email = localStorage.getItem('userEmail');
    if (email) {
      this.authService.getUserInfo(email).subscribe({
        next: (response) => {
          this.user.name = response.username;
          this.user.email = response.email;
        },
        error: (err) => {
          console.error('Error al obtener los datos del usuario:', err);
        },
      });
    }
  }

  openPopup() {
    this.isPopupVisible = true;
  }

  closePopup() {
    this.isPopupVisible = false;
  }

  navigateToSurvey() {
    this.isPopupVisible = false;
    this.router.navigate(['/survey']);
  }
}

