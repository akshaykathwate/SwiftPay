import { Component } from '@angular/core';
import { Router } from '@angular/router'; // Import Router for navigation
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  showModal: boolean = false;

  constructor(private router: Router) {}

  logout() {
    this.showModal = true;
  }

  handleLogoutConfirmation(confirmed: boolean) {
    if (confirmed) {
      localStorage.removeItem('user');

      this.router.navigate(['/login']);
    }
    this.showModal = false;
  }
}
