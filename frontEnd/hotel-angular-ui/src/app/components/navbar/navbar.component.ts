import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { ApiService } from '../../service/api.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  constructor(private router: Router, private apiService: ApiService) { }

  get isAuthenticated(): boolean {
    return this.apiService.isAuthenticated();
  }

  get isAdmin(): boolean {
    return this.apiService.isAdmin();
  }

  get isCustomer(): boolean {
    return this.apiService.isCustomer();
  }


  handleLogout(): void {
    const isLogout = window.confirm('Are you sure you want to logout?');
    if (isLogout) {
      this.apiService.logout();
      this.router.navigate(['/home']);
    }
  }



}
