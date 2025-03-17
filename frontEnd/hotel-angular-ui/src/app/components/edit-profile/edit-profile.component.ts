import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../service/api.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-profile',
  imports: [CommonModule],
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.css'
})
export class EditProfileComponent {

  user: any = null;
  error: any = null;

  constructor(private apiService: ApiService, private router: Router) { }

  ngOnInit(): void {
    this.fetchUserProfile();
  }

  // Fetch user profile on component initialization
  fetchUserProfile(): void {
    this.apiService.myProfile().subscribe({
      next: (response: any) => {
        this.user = response.user;
        console.log(this.user); // for debugging
      },
      error: (err) => {
        this.showError(err?.error?.message || 'Error fetching user profile');
      },
    });
  }

  showError(message: string) {
    this.error = message;
    setTimeout(() => {
      this.error = null;
    }, 4000);
  }

  // Handle account deletion
  handleDeleteProfile(): void {
    if (
      !window.confirm(
        'Are you sure you want to delete your account? If you delete your account, you will lose access to your profile and booking history.'
      )
    ) {
      return;
    }

    this.apiService.deleteAccount().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.showError(err?.error?.message || 'Error Deleting account');
      },
    });
  }

}

