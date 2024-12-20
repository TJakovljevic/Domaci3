import {Component, OnInit} from '@angular/core';
import {PermissionsService} from "../../services/permissions.service";
import {Permission} from "../../model";
import {filter} from "rxjs";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  current_user: string = ""
  permissions: Permission[] = []
  canCreateUsers: boolean = false;


  constructor(private router: Router,private permissionsService: PermissionsService) {}
  checkPermissions() {

      this.canCreateUsers = this.permissions.some((permission: {
        name: string;
      }) => permission.name === 'can_create_users');


      console.log(this.canCreateUsers)

  }

  getUser(){
    const token = localStorage.getItem("authToken")
    if (token != null) {

      const decodedToken = JSON.parse(atob(token.split('.')[1]));
      this.current_user = decodedToken.sub || "Guest";
      console.log(this.current_user);
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


  ngOnInit(): void {


    this.getUser();
    this.fetchPermissions()

  }

}
