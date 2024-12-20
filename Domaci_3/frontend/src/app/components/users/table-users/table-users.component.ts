import {Component, OnInit} from '@angular/core';
import {Permission, User} from "../../../model";
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


  canReadCheck(){
    if (this.canReadUsers) {
      this.tableUsersService.fetchUsers().subscribe(
        (users) => {
          this.users = users;
        },
        (error) => {
          console.error(error);
        }
      );
    }else{
      alert("You don't have the permission to read user data!")
      this.router.navigate([''])
    }
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
