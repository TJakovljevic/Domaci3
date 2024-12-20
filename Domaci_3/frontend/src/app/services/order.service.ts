import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root',
})
export class OrderService {

    private readonly apiUrl = environment.api;
    private readonly addedUrl = "dish"
    private jwtToken: string | null = null;
    private current_user = "";
    constructor(private http: HttpClient) {
        this.jwtToken = localStorage.getItem("authToken");
        this.getUser();
    }

    fetchMenu():Observable<any>{
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${this.jwtToken}`
        });
        const url = `${this.apiUrl}${this.addedUrl}`
        return this.http.get(url, { headers });
    }

    submitOrder(body: { createdBy: { email: string }; active: boolean; dishes: Array<number> }): Observable<any>{
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${this.jwtToken}`
        });
        body.createdBy.email = this.current_user;
        console.log(body)
        return this.http.post(this.apiUrl + "order", body,{ headers });
    }

    scheduleOrder(body: { createdBy: { email: string }; active: boolean; dishes: Array<number>; scheduledTime:string}): Observable<any>{
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${this.jwtToken}`
        });
        body.createdBy.email = this.current_user;
        console.log(body)
        return this.http.post(this.apiUrl + "order/schedule", body,{ headers });
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