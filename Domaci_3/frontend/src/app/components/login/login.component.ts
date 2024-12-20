import {Component, OnInit} from '@angular/core';
import {LoginService} from "../../services/login.service";
import {JwtToken, Permission} from "../../model";
import {PermissionsService} from "../../services/permissions.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  email: string = "";
  password: string = "";
  results: string = "";
  token: string | null = null;
  permissions: Permission[] = [];

  canReadUsers: boolean = false;
  canCreateUsers: boolean = false;
  canUpdateUsers: boolean = false;
  canDeleteUsers: boolean = false;

  checkPermissions() {


      this.canReadUsers = this.permissions.some((permission: { name: string;
      }) => permission.name === 'can_read_users');
      this.canCreateUsers = this.permissions.some((permission: {
        name: string;
      }) => permission.name === 'can_create_users');
      this.canUpdateUsers = this.permissions.some((permission: {
        name: string;
      }) => permission.name === 'can_update_users');
      this.canDeleteUsers = this.permissions.some((permission: {
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


  constructor(private loginService: LoginService, private permissionsService: PermissionsService) {
    this.token = localStorage.getItem("authToken")
  }

  ngOnInit(): void {
    this.token = localStorage.getItem("authToken")
  console.log("Permissions: " + this.token)
    if(this.token){
          this.fetchPermissions()
        }
    }


  sendRequest(): void{
    this.loginService.login(this.email, this.password).
    subscribe(
      response=>{
        this.results = response.jwt;
        console.log("Login successful: " + this.results)
      },
      error => {
        console.error('Error fetching detection data:', error);
      });
  }

  fetchPermissions(){
    this.permissionsService.fetchPermissions().
    subscribe(
      (response: Permission[]) => {
        this.permissions = response;
        console.log(this.permissions)
        console.log(localStorage.getItem("authToken"))
        this.checkPermissions();

      },
      error => {
        console.error('Error fetching permissions:', error);
      }
    );
  }
  logout(): void{
    this.loginService.logout();
    this.results = "";
    console.log(this.results)
  }
}
