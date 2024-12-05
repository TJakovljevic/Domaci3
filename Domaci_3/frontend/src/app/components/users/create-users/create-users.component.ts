import {Component, OnInit} from '@angular/core';
import {CreateUserService} from "../../../services/create-user.service";
import {Permission, User, UserDto} from "../../../model";
import {Router} from "@angular/router";


@Component({
  selector: 'app-create-users',
  templateUrl: './create-users.component.html',
  styleUrls: ['./create-users.component.css']
})
export class CreateUsersComponent implements OnInit{


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
    }
  }



  availablePermissions:Permission[] = [];
  user: UserDto = {
    first_name: '',
    last_name: '',
    email: '',
    password: '',
    permissions: [] as Permission[]
  };

  errorMessage: string = '';

  ngOnInit(): void {
    this.checkPermissions()
    if(!this.canCreateUsers){
      alert("You don't have the permission for this action!")
      this.router.navigate(['']);
    }
    this.fetchPermissions();

  }


  constructor(private createUserService: CreateUserService, private router: Router) {}


  togglePermission(permission: Permission) {
    const index = this.user.permissions.indexOf(permission);
    if (index === -1) {
      this.user.permissions.push(permission);
    } else {
      this.user.permissions.splice(index, 1);
    }
  }

  fetchPermissions(){
    this.createUserService.fetchPermissions().
    subscribe(( response)=>{
     this.availablePermissions = response;

    },(error) => {
      this.errorMessage = 'Error fetching permissions. Please try again.';
      console.error(error);
    });
  }
  createUser() {

    if (!this.user.first_name.trim()) {
      this.errorMessage = 'First Name is required.';
      return;
    }
    if (!this.user.last_name.trim()) {
      this.errorMessage = 'Last Name is required.';
      return;
    }
    if (!this.user.email.trim()) {
      this.errorMessage = 'Email is required.';
      return;
    }
    if (!this.user.email.endsWith('@gmail.com')) {
      this.errorMessage = 'Email must be a @gmail.com address.';
      return;
    }

    const permissionIds = this.user.permissions.map((permission) => permission.id);

    const userDto = {
      ...this.user,
      permissions: permissionIds
    };
    this.createUserService.createUser(userDto).subscribe(
      () => {
        alert("User successfully added!");
        this.router.navigate(['table-users'])
      },
      (error) => {
        this.errorMessage = 'Error creating user. Please try again.';
        console.error(error);
      }
    );
  }


}
