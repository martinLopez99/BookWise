import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  isLoggedIn = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.authService.isAuthenticated$.subscribe((status) => {
      this.isLoggedIn = status;
    });
  }

  redirectToLogin() {
    this.router.navigate(['/auth']);
  }

  logout() {
    this.authService.logout();
    localStorage.removeItem('userEmail');
    localStorage.removeItem('token');
    localStorage.removeItem('userPreferredGenre');
    this.router.navigate(['/home']).then(() => {
      window.location.reload();
    });
  }
}
