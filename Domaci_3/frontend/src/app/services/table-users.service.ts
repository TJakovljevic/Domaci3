import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class TableUsersService {

  private readonly apiUrl = environment.api;
  private readonly addedUrl = "users";
  private jwtToken: string | null = null;
  constructor(private http: HttpClient) {
    this.jwtToken = localStorage.getItem("authToken");

  }

  fetchUsers(): Observable<any>{
    console.log("Token: " + this.jwtToken)
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.jwtToken}`  // Bearer token
    });

      return this.http.get(this.apiUrl + this.addedUrl, { headers });
  }

  deleteUser(id: number): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.jwtToken}`  // Bearer token
    });
    return this.http.delete(`${this.apiUrl}${this.addedUrl}/${id}`, { headers });
  }
}
