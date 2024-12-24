import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";
import {Search} from "../model";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root',
})
export class SearchService {


    private readonly apiUrl = environment.api;
    private readonly addedUrl = "order";
    private readonly permissionsUrl = "permissions"
    private jwtToken: string | null = null;
    private current_user: string = "";
    private isAdmin: boolean = false;

    constructor(private http: HttpClient, private router: Router) {
        this.jwtToken = localStorage.getItem("authToken");
        this.getUser();
    }

    cancelOrder(id: number):Observable<any>{
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${this.jwtToken}`  // Bearer token
        });

        return this.http.put(this.apiUrl+this.addedUrl+"/" +id,null, { headers });
    }
    searchOrder(page: number, size: number, body: Search): Observable<any>{
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${this.jwtToken}`  // Bearer token
        });


        if(!this.isAdmin) {
            body.email = this.current_user;
        }
        else if(this.isAdmin && body.email == ""){
            body.email = "";
        }
        console.log("Search body email: " + body.email);
        console.log("Search body status: " + body.status);
        console.log("Search body dateTo: " + body.dateTo);
        console.log("Search body dateFrom: " + body.dateFrom);

        return this.http.post(`${this.apiUrl}${this.addedUrl}/search?page=${page}&size=${size}`, body, { headers })
    }

    fetchUsers(): Observable<any>{
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${this.jwtToken}`
        });
        return this.http.get(this.apiUrl+"users", { headers })
    }

    fetchOrders():Observable<any>{
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${this.jwtToken}`  // Bearer token
        });
        return this.http.get(this.apiUrl + this.addedUrl, { headers })
    }

    getUser(){
        const token = this.jwtToken;
        if (token != null) {

            const decodedToken = JSON.parse(atob(token.split('.')[1]));
            this.current_user = decodedToken.sub || "Guest";
            this.isAdmin = decodedToken.admin || "";
            console.log(this.current_user);
        }
    }

}
