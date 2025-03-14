import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../service/api.service';

@Component({
  selector: 'app-room-result',
  imports: [CommonModule],
  templateUrl: './room-result.component.html',
  styleUrl: './room-result.component.css'
})
export class RoomResultComponent {

  @Input() roomSearchResults: any[] = [];  // input property for the room results
  isAdmin: boolean;

  constructor(private apiService: ApiService, private router: Router) {
    // get the current user's admin status
    this.isAdmin = this.apiService.isAdmin();
  }

  // method to navigate to the edit room page (for admins)
  navigateToEditRoom(roomId: string) {
    this.router.navigate([`/admin/edit-room/${roomId}`]);
  }


  // method to navigate to the room details page (for users)
  navigateToRoomDetails(roomId: string) {
    this.router.navigate([`/room-details/${roomId}`]);
  }

}
