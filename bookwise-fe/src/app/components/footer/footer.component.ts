import { Component, HostListener, OnInit } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
})
export class FooterComponent implements OnInit {
  isVisible: boolean = false; // Estado para mostrar/ocultar el footer

  ngOnInit(): void {}

  // Listener del evento de scroll
  @HostListener('window:scroll', [])
  onScroll(): void {
    const scrollPosition =
      window.innerHeight + window.scrollY; // Altura visible más el desplazamiento
    const pageHeight = document.documentElement.scrollHeight; // Altura total del contenido

    // Mostrar el footer si se llega al final de la página
    this.isVisible = scrollPosition >= pageHeight - 50;
  }
}
