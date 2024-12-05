import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  current_user: string = ""

  canReadUsers: boolean = false;
  canCreateUsers: boolean = false;
  canUpdateUsers: boolean = false;
  canDeleteUsers: boolean = false;

  checkPermissions() {
    const token = localStorage.getItem("authToken")
    if (token != null) {

      const decodedToken = JSON.parse(atob(token.split('.')[1]));
      this.current_user = decodedToken.sub || "Guest";
      console.log(this.current_user);

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


  ngOnInit(): void {
    this.checkPermissions()
  }

}
