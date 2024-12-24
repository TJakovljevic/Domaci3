import {Component, OnInit} from '@angular/core';
import {PermissionsService} from "../../../services/permissions.service";
import {SearchService} from "../../../services/search.service";
import {StatusUpdate, OrderEntity, Permission, User, PaginatedResponseOrder} from "../../../model";
import {Router} from "@angular/router";
import {CompatClient, Stomp} from "@stomp/stompjs";
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit{

    isAdmin: boolean = false;
    email: string = ""
    status: Set<string>  = new Set();
    dateFrom: string = "";
    dateTo: string = ""
    statusOptions: string[] = ["ORDERED", "PREPARING", "IN_DELIVERY", "DELIVERED", "CANCELED"]
    can_search_order: boolean = false;
    can_cancel_order: boolean = false;
    can_track_order: boolean = false;
    permissions: Permission[] = [];
    orders: OrderEntity[] = [];
    users: User[] = [];
    // @ts-ignore
    stompClient: CompatClient;
    isConnected: boolean = false;
    messages: StatusUpdate[] = []
    currentPage: number = 0;
    itemsPerPage: number = 5;
    totalPages: number = 0;


connect(){
    const jwt = localStorage.getItem("authToken");
    const socket = new SockJS("http://localhost:8080/ws?jwt=" + jwt);
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, this.onConnect.bind(this));
}

onConnect(frame: any){
    this.stompClient.subscribe('/topic/orders', this.addNewMessage.bind(this));
    this.isConnected = true;
    alert("Tracking enabled")
    console.log('Connected: ' + frame);
}

disconnect() {
    if(this.stompClient != null) {
        this.stompClient.disconnect();
    }
    this.isConnected = false;
    alert("Tracking disabled")
    console.log("Disconnected");
}

addNewMessage(messageOutput: any) {
    const statusUpdate = JSON.parse(messageOutput.body);
    console.log("Status update", statusUpdate)
    console.log("Status update id", statusUpdate.orderId)
    console.log("Status update status", statusUpdate.status)

    if (statusUpdate.orderId && statusUpdate.status) {
        const order = this.orders.find(o => o.id === statusUpdate.orderId);

        if (order) {
            order.status = statusUpdate.status;

            console.log(`Updated order ${order.id} to status: ${order.status}`);
        } else {
            console.warn(`Order with ID ${statusUpdate.id} not found.`);
        }
    } else {
        console.warn("Received message is not a valid StatusUpdate:", statusUpdate);
    }


    this.messages.push(statusUpdate);
}

  ngOnInit(): void {
    this.fetchPermissions()
    this.getAdmin()
  }

  constructor(private router: Router,private permissionsService: PermissionsService, private searchService: SearchService) {}


  checkPermissions()
  {
    this.can_search_order = this.permissions.some((permission: Permission) =>
        permission.name === 'can_search_order'
    );
    this.can_cancel_order = this.permissions.some((permission: Permission) =>
      permission.name === 'can_cancel_order'
    );
    this.can_track_order = this.permissions.some((permission: Permission) =>
      permission.name === 'can_track_order'
    );

    this.canSearchCheck()

  }

    pageChanged(page: number): void {
        this.currentPage = page;
        this.searchOrders();
    }
  cancelOrder(id: number){
        this.searchService.cancelOrder(id).subscribe(
            (response) => {

                alert("Order successfully canceled")
                window.location.reload()
            },
            (error) => {
                alert("Error cancelling orders");
            }
        )
  }

  toggleStatuses(status: string) {
    if (this.status.has(status)) {
      this.status.delete(status);
    } else {
      this.status.add(status);
    }
  }

  canSearchCheck(){
    if (this.can_search_order) {
      this.searchOrders();
      this.fetchUsers()
    }else{
      alert("You don't have the permission to search orders!")
      this.router.navigate([''])
    }
  }

  getAdmin(){
    const token = localStorage.getItem("authToken");
    if (token != null) {
      const decodedToken = JSON.parse(atob(token.split('.')[1]));
      this.isAdmin = decodedToken.admin || "";
      console.log(this.isAdmin)
    }
  }

  fetchUsers() {
    this.searchService.fetchUsers()
        .subscribe(
            (response: User[]) => {
              this.users = response;
              console.log(this.users)


            },
            error => {
              console.error('Error fetching users:', error);
            }
        )
  }


  searchOrders(){


    const body={
      email: this.email,
      status: Array.from(this.status),
      dateFrom: this.dateFrom,
      dateTo: this.dateTo
    }

      this.searchService.searchOrder(this.currentPage, this.itemsPerPage, body).subscribe(
          (response: PaginatedResponseOrder) => {
              this.orders = response.content;
              this.totalPages = response.totalPages;
              console.log("Search Results: ", this.orders);
          },
          (error) => {
              console.error("Error searching orders:", error);
              alert("Failed to search orders. Please try again.");
          }
      );
  }

  fetchPermissions(){
    this.permissionsService.fetchPermissions().
    subscribe(
        (response: Permission[]) => {
          this.permissions = response;
          console.log(this.permissions)

          this.checkPermissions();

        },
        error => {
          console.error('Error fetching permissions:', error);
        }
    );
  }




}
