import { Component, OnInit } from '@angular/core';
import { Permission, UserDto } from "../../../model";
import { ActivatedRoute, Router } from "@angular/router";
import { EditUserService } from "../../../services/edit-user.service";
import { forkJoin } from 'rxjs';
import {PermissionsService} from "../../../services/permissions.service";

@Component({
  selector: 'app-edit-users',
  templateUrl: './edit-users.component.html',
  styleUrls: ['./edit-users.component.css']
})
export class EditUsersComponent implements OnInit {

  route_id: string = "";
  current_user: string = "";

  user: UserDto = {
    first_name: '',
    last_name: '',
    email: '',
    password: '',
    permissions: []
  };

  permissions: Permission[] = [];

  availablePermissions: Permission[] = [];
  selectedPermissions: Set<number> = new Set();

  errorMessage: string = '';
  updated_jwt: string ="";
  canUpdateUsers: boolean = false;

  constructor(
    private editUserService: EditUserService,
    private router: Router,
    private route: ActivatedRoute,
    private permissionsService: PermissionsService
  ) {}

  ngOnInit(): void {

    this.getUser();
    this.fetchPermissions();
    this.loadData();
  }

  checkPermissions()
  {

    this.canUpdateUsers = this.permissions.some((permission: Permission) =>
      permission.name === 'can_update_users'
    );

    this.canUpdateCheck();
    console.log(this.canUpdateUsers)
  }


  fetchPermissions(){
    console.log(this.current_user)
    this.permissionsService.fetchPermissions(this.current_user).
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

  getUser(){
    const token = localStorage.getItem("authToken")
    if (token != null) {

      const decodedToken = JSON.parse(atob(token.split('.')[1]));
      this.current_user = decodedToken.sub || "Guest";
      console.log(this.current_user);
    }
  }

  canUpdateCheck(){
    if (!this.canUpdateUsers) {
      console.log("!canUpdate")
      alert("You don't have the permission for this action!");
      this.router.navigate(['']);
    }

  }



  loadData() {
    this.route_id = this.route.snapshot.paramMap.get('id') || '';

    forkJoin([
      this.editUserService.fetchUser(this.route_id),
      this.editUserService.fetchPermissions()
    ]).subscribe(
      ([userResponse, permissionsResponse]) => {
        this.user = userResponse;
        this.availablePermissions = permissionsResponse;

        this.selectedPermissions = new Set(this.user.permissions.map(permission => permission.id));

      },
      error => {
        console.error('Error loading data:', error);
      }
    );
  }

  togglePermission(permissionId: number) {
    if (this.selectedPermissions.has(permissionId)) {
      this.selectedPermissions.delete(permissionId);
    } else {
      this.selectedPermissions.add(permissionId);
    }
  }

  updateUser() {

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

    const updatedPermissions: Permission[] = Array.from(this.selectedPermissions).map(permissionId => {
      return this.availablePermissions.find(permission => permission.id === permissionId) as Permission;
    });

    const updatedUser: UserDto = {
      ...this.user,
      permissions: updatedPermissions
    };

    this.editUserService.updateUser(this.route_id, updatedUser).subscribe(
      response => {
        alert('User updated successfully!');
        this.updated_jwt = response.token;
        window.location.reload()
        // this.router.navigate(['/table-users']);



      },
      error => {
        console.error('Error updating user:', error);
      }
    );
  }
}
