import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root',
})
export class ErrorService {

    private readonly apiUrl = environment.api;
    private readonly addedUrl = "error/"
    private jwtToken: string | null = null;
    private current_user = "";
    constructor(private http: HttpClient) {
        this.jwtToken = localStorage.getItem("authToken");
        this.getUser();
    }



    //Posle dodaj za paginaciju variable
    fetchErrors(page: number, size: number): Observable<any> {
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${this.jwtToken}`
        });
        const url = `${this.apiUrl}${this.addedUrl}${this.current_user}?page=${page}&size=${size}`;
        return this.http.get(url, { headers });
    }

    getUser(){
        const token = this.jwtToken;
        if (token != null) {

            const decodedToken = JSON.parse(atob(token.split('.')[1]));
            this.current_user = decodedToken.sub || "Guest";
            console.log(this.current_user);
        }
    }


}