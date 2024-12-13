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


  constructor(private http: HttpClient) {
    this.jwtToken = localStorage.getItem("authToken");
  }

  fetchPermissions(email: string): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.jwtToken}`  // Bearer token
    });

    return this.http.get(this.apiUrl + this.addedUrl + email, { headers });
  }


}

