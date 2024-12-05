import { Component } from '@angular/core';
import {LoginService} from "../../services/login.service";
import {JwtToken} from "../../model";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email: string = "";
  password: string = "";
  results: string = "";
  token: string | null = null;

  canReadUsers: boolean = false;
  canCreateUsers: boolean = false;
  canUpdateUsers: boolean = false;
  canDeleteUsers: boolean = false;

  checkPermissions() {
    const token = localStorage.getItem("authToken")
    if (token != null) {

      const decodedToken = JSON.parse(atob(token.split('.')[1]));


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
      if(!this.canReadUsers && !this.canCreateUsers && !this.canDeleteUsers
      && !this.canUpdateUsers){
        alert("You don't have any permissions!")
      }
    }
  }

  constructor(private loginService: LoginService) {
    this.token = localStorage.getItem("authToken")
  }

  sendRequest(): void{
    this.loginService.login(this.email, this.password).
    subscribe(
      response=>{
        this.results = response.jwt;
        console.log("Login successful: " + this.results)
        this.checkPermissions()
      },
      error => {
        console.error('Error fetching detection data:', error);
      });
  }

  logout(): void{
    this.loginService.logout();
    this.results = "";
    console.log(this.results)
  }
}
