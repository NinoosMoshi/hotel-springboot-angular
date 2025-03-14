import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  imports: [CommonModule],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {

  @Input() roomPerPage: number = 10;  // number of rooms per page
  @Input() totalRooms: number = 0;    // total number of rooms
  @Input() currentPage: number = 1;   // current page
  @Output() paginate: EventEmitter<number> = new EventEmitter<number>();  // emoit page number

  // method to generate the page numbers
  get pageNumbers(): number[] {
    const pageCount = Math.ceil(this.totalRooms / this.roomPerPage);
    return Array.from({ length: pageCount }, (_, i) => i + 1);
  }

  // method to handle page change
  onPageChange(pageNumber: number): void {
    this.paginate.emit(pageNumber);  // emit the page number to parent component
  }

}
