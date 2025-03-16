import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ApiService } from '../../service/api.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  constructor(private apiService: ApiService, private router: Router) { }

  formData: any = {
    firstName: '',
    lastName: '',
    phoneNumber: '',
    email: '',
    password: '',
  };

  error: any = null;

  handleSubmit() {
    if (!this.formData.firstName || !this.formData.lastName || !this.formData.phoneNumber || !this.formData.email || !this.formData.password) {
      this.showEerror('All fields are required');
      return;
    }

    this.apiService.registerUser(this.formData).subscribe({
      next: (response) => {
        this.router.navigateByUrl('/login');
      },
      error: (err) => {
        this.showEerror(err?.error?.message || err.message || 'Error registering user' + err);
      }
    })
  }


  showEerror(msg: string): void {
    this.error = msg;
    setTimeout(() => {
      this.error = null;
    }, 4000);
  }

}









