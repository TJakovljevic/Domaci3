import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {catchError, Observable, tap} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {JwtToken} from "../model";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root',
})
export class LoginService{

  private readonly apiUrl = environment.api;
  private readonly addedUrl = "auth/login";
  private jwtToken: string = "";

  constructor(private http: HttpClient,private router: Router) {
  }
  login(email: string, password: string): Observable<any> {
    const body = { email, password };
    const url = this.apiUrl + this.addedUrl;

    return this.http.post<any>(url, body).pipe(
      tap((response) => {
        alert("Successful login!");
        this.jwtToken = response.jwt;
        localStorage.setItem("authToken", this.jwtToken);
        window.location.reload();
        // this.router.navigate([""]);
      }),
      catchError(
        (error) => {
        alert("User doesn't exist!");
        console.error('Error during login:', error);
        throw error;
      })
    );
  }

  logout(){
    localStorage.removeItem("authToken");
    window.location.reload();
    // this.router.navigate([""]);
  }
}
