import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../service/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  constructor(private apiService: ApiService, private router: Router) { }

  formData: any = {
    email: '',
    password: ''
  };

  error: any = null;

  handleSubmit() {
    if (!this.formData.email || !this.formData.password) {
      this.showEerror('All fields are required');
      return;
    }

    this.apiService.loginUser(this.formData).subscribe({
      next: (response) => {
        if (response.status === 200) {
          this.apiService.encryptAndSaveStorage('token', response.token);
          this.apiService.encryptAndSaveStorage('role', response.role);
          this.router.navigate(['/home']);
        }
      },
      error: (err) => {
        this.showEerror(err?.error?.message || err.message || 'Error logging in' + err);
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
