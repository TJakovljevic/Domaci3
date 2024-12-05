import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {User, UserDto} from "../model";

@Injectable({
  providedIn: 'root',
})
export class EditUserService {

  private readonly apiUrl = environment.api;
  private readonly addedUrl = "users/";
  private readonly permissionsUrl = "permissions"
  private jwtToken: string | null = null;

  constructor(private http: HttpClient) {
    this.jwtToken = localStorage.getItem("authToken");

  }
  fetchPermissions(): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.jwtToken}`
    });
    return this.http.get(this.apiUrl + this.permissionsUrl, { headers });
  }

  fetchUser(id:string): Observable<any>{
    console.log("Token: " + this.jwtToken)
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.jwtToken}`
    });

    return this.http.get(this.apiUrl + this.addedUrl+ id, { headers });
  }

  updateUser(id: string, user: UserDto):Observable<any>{
    console.log("Id :" + id + ", " + "User:" + user)
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.jwtToken}`
    });
    const body = {
      email: user.email,
      first_name: user.first_name,
      last_name: user.last_name,
      password: user.password,
      permissions: user.permissions.map(permission => permission.id)
    };

    console.log(body)


    return this.http.put(this.apiUrl+this.addedUrl+id,body, {headers} );
  }
}
