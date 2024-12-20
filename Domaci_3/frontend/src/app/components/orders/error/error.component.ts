import {Component, OnInit} from '@angular/core';
import {PermissionsService} from "../../../services/permissions.service";
import {ErrorMessage, PaginatedResponse, Permission} from "../../../model";
import {Router} from "@angular/router";
import {ErrorService} from "../../../services/error.service";


@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit{

    errors: ErrorMessage[] = [];
    currentPage: number = 0;
    itemsPerPage: number = 5;
    totalPages: number = 0;



  ngOnInit(): void {
    this.fetchErrors(this.currentPage, this.itemsPerPage);
  }
    constructor(private errorService: ErrorService, private permissionsService: PermissionsService, private router: Router) {
    }



  fetchErrors(page: number, size: number){
    this.errorService.fetchErrors(page, size).
    subscribe(
        (response: PaginatedResponse) =>{
          this.errors = response.content;
          this.totalPages = response.totalPages;
          console.log(this.errors);
        },
    error => {
      console.error('Error fetching errors:', error);
    }
    )
  }

    pageChanged(page: number): void {
        this.currentPage = page;
        this.fetchErrors(this.currentPage, this.itemsPerPage);
    }

}
