
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent {

  username = '';
  email = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}

  onRegister() {
    this.authService.register(this.username, this.email, this.password).subscribe({
      next: (response) => {
        console.log('Registro exitoso:', response);
        alert('Usuario registrado correctamente. Ahora puedes iniciar sesión.');
        localStorage.setItem('userEmail', this.email);
        this.router.navigate(['/auth']);
      },
      error: (err) => {
        console.error('Error al registrar usuario:', err);
        alert('Error al registrar usuario');
      },
    });
  }


  onCancel() {
    this.router.navigate(['/auth']);
  }

  goBackToLogin() {
    this.router.navigate(['/auth']);
  }
}
