import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../service/api.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile',
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

  user: any = null;
  bookings: any[] = [];
  error: any = null;

  constructor(private apiService: ApiService, private router: Router) { }

  ngOnInit(): void {
    this.fetchUserProfile();
  }

  // Fetch user profile and bookings
  fetchUserProfile() {
    this.apiService.myProfile().subscribe({
      next: (response: any) => {
        this.user = response.user;
        // Fetch bookings after the user profile is fetched
        this.apiService.myBookings().subscribe({
          next: (bookingResponse: any) => {
            this.bookings = bookingResponse.bookings;
          },
          error: (err) => {
            this.showError(
              err?.error?.message ||
              err?.error ||
              'Error getting my bookings: ' + err
            );
          },
        });
      },
      error: (err) => {
        this.showError(
          err?.error?.message ||
          err?.error ||
          'Error getting my profile info: ' + err
        );
      },
    });
  }

  // Handle errors
  showError(msg: string) {
    this.error = msg;
    setTimeout(() => {
      this.error = null;
    }, 4000);
  }

  // Handle logout
  handleLogout() {
    this.apiService.logout();
    this.router.navigate(['/home']);
  }

  // Navigate to edit profile page
  handleEditProfile() {
    this.router.navigate(['/edit-profile']);
  }
}
