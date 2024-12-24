import {Component, OnInit} from '@angular/core';
import {PaginatedResponseUser, Permission, User} from "../../../model";
import {TableUsersService} from "../../../services/table-users.service";
import {Router} from "@angular/router";
import {PermissionsService} from "../../../services/permissions.service";



@Component({
  selector: 'app-table-users',
  templateUrl: './table-users.component.html',
  styleUrls: ['./table-users.component.css']
})
export class TableUsersComponent implements OnInit {



    users: User[] = []
    canReadUsers: boolean = false;
    canCreateUsers: boolean = false;
    canUpdateUsers: boolean = false;
    canDeleteUsers: boolean = false;
    private permissions: Permission[] = []
    currentPage: number = 0;
    itemsPerPage: number = 5;
    totalPages: number = 0;


  checkPermissions()
    {
      this.canReadUsers = this.permissions.some((permission: Permission) =>
        permission.name === 'can_read_users'
      );

      this.canCreateUsers = this.permissions.some((permission: Permission) =>
        permission.name === 'can_create_users'
      );

      this.canUpdateUsers = this.permissions.some((permission: Permission) =>
        permission.name === 'can_update_users'
      );

      this.canDeleteUsers = this.permissions.some((permission: Permission) =>
        permission.name === 'can_delete_users'
      );

      this.canReadCheck()
      console.log(this.canCreateUsers)
      console.log(this.canReadUsers)
      console.log(this.canDeleteUsers)
      console.log(this.canUpdateUsers)
    }


    ngOnInit(): void {
      this.fetchPermissions()

  }

  fetchUsers(){
      this.tableUsersService.fetchUsers(this.currentPage, this.itemsPerPage).subscribe(
          (users: PaginatedResponseUser) => {
              this.users = users.content;
              this.totalPages = users.totalPages;
          },
          (error) => {
              console.error(error);
          }
      );
  }

  canReadCheck(){
    if (this.canReadUsers) {
        this.fetchUsers()
    }else{
      alert("You don't have the permission to read user data!")
      this.router.navigate([''])
    }
  }

    pageChanged(page: number): void {
        this.currentPage = page;
        this.fetchUsers();
    }
  fetchPermissions(){
    this.permissionsService.fetchPermissions().
    subscribe(
      (response: Permission[]) => {
        this.permissions = response;
        console.log(this.permissions)

        this.checkPermissions();

      },
      error => {
        console.error('Error fetching permissions:', error);
      }
    );
  }

  constructor(private tableUsersService: TableUsersService, private router: Router, private permissionsService: PermissionsService) {}


    deleteUser(id: number){
    this.tableUsersService.deleteUser(id).
    subscribe(
        response=>{
          alert("User deleted successfully!")
          window.location.reload()
        },
        error => {
          console.error('Error fetching detection data:', error);
        });

    }
}
