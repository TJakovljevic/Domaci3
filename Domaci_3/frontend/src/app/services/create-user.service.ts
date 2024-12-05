import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User, UserDto} from "../model";
import {Observable} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root',
})
export class CreateUserService {


  private readonly apiUrl = environment.api;
  private readonly addedUrl = "users";
  private readonly permissionsUrl = "permissions"
  private jwtToken: string | null = null;

  constructor(private http: HttpClient, private router: Router) {
    this.jwtToken = localStorage.getItem("authToken");
  }

  fetchPermissions(): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.jwtToken}`  // Bearer token
    });
    return this.http.get(this.apiUrl + this.permissionsUrl, { headers });
  }

  //stavio sam ovako da bi podrzalo [1, 2, 3]
  createUser(user: {
    password: string;
    permissions: number[];
    last_name: string;
    first_name: string;
    email: string
  }): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.jwtToken}`,
      'Content-Type': 'application/json',
    });
    const body =  user
    console.log(body)
    return this.http.post(`${this.apiUrl}${this.addedUrl}`, body,{ headers });
  }

}
