import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class PermissionsService {

  private readonly apiUrl = environment.api;
  private readonly addedUrl = "users/permissions/";
  private jwtToken: string | null = null;
  private current_user = "";

  constructor(private http: HttpClient) {
    this.jwtToken = localStorage.getItem("authToken");
    this.getUser();
  }

  getUser(){
    const token = this.jwtToken;
    if (token != null) {

      const decodedToken = JSON.parse(atob(token.split('.')[1]));
      this.current_user = decodedToken.sub || "Guest";
      console.log(this.current_user);
    }
  }

  fetchPermissions(): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.jwtToken}`  // Bearer token
    });

    return this.http.get(this.apiUrl + this.addedUrl + this.current_user, { headers });
  }


}

