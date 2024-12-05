import {Component, OnInit} from '@angular/core';
import {User} from "../../../model";
import {TableUsersService} from "../../../services/table-users.service";
import {Router} from "@angular/router";

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

    checkPermissions() {
      const token = localStorage.getItem("authToken")
      if (token != null) {
        const decodedToken = JSON.parse(atob(token.split('.')[1]));
        console.log(decodedToken);

        const permissions = decodedToken.permissions || [];

        this.canReadUsers = permissions.some((permission: { name: string;
        }) => permission.name === 'can_read_users');
        this.canCreateUsers = permissions.some((permission: {
          name: string;
        }) => permission.name === 'can_create_users');
        this.canUpdateUsers = permissions.some((permission: {
          name: string;
        }) => permission.name === 'can_update_users');
        this.canDeleteUsers = permissions.some((permission: {
          name: string;
        }) => permission.name === 'can_delete_users');

        console.log(this.canReadUsers)
        console.log(this.canCreateUsers)
        console.log(this.canUpdateUsers)
        console.log(this.canDeleteUsers)
      }else {
          // alert('You do not have any permissions.');
        }
    }


  ngOnInit(): void {

    this.checkPermissions();
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

  constructor(private tableUsersService: TableUsersService, private router: Router) {}
    fetchUsers(){
      this.tableUsersService.fetchUsers().
      subscribe(
            response=>{
        this.users = response;

      },
      error => {
        console.error('Error fetching detection data:', error);
      });

    }

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
