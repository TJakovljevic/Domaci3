import {Component, OnInit} from '@angular/core';
import {Dish, Permission} from "../../../model";
import {HttpClient} from "@angular/common/http";
import {PermissionsService} from "../../../services/permissions.service";
import {Router} from "@angular/router";
import {OrderService} from "../../../services/order.service";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {


  dishes: Dish[] = []
  permissions: Permission[] = [];
  selectedDishes: Set<number> = new Set();
  can_place_order: boolean = false;
  can_schedule_order: boolean = false;
  scheduledDate: string = "";


  constructor(private router: Router, private permissionsService: PermissionsService, private orderService: OrderService) {}

  ngOnInit(): void {
    this.fetchPermissions();

  }

  checkPermissions()
  {
    this.can_place_order = this.permissions.some((permission: Permission) =>
        permission.name === 'can_place_order'
    );
    this.can_schedule_order = this.permissions.some((permission: Permission) =>
        permission.name === 'can_schedule_order'
    );

    this.canOrderCheck()

  }

  togglePermission(permissionId: number) {
    if (this.selectedDishes.has(permissionId)) {
      this.selectedDishes.delete(permissionId);
    } else {
      this.selectedDishes.add(permissionId);
    }
  }

  canOrderCheck(){
    if (this.can_place_order) {
      this.fetchMenu();
    }else{
      alert("You don't have the permission to read user data!")
      this.router.navigate([''])
    }
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

  submitOrder(){

    const body = {
      createdBy: {
        email: '',
      },
      active: true,
      dishes: Array.from(this.selectedDishes),
    };


    this.orderService.submitOrder(body).
    subscribe(
        (response) => {
          console.log('Order submitted successfully', response);
        },
        error => {
          console.error('Error submitting order:', error);
        }
    );
  }

  scheduleOrder(){
    const body = {
      createdBy: {
        email: '',
      },
      active: true,
      dishes: Array.from(this.selectedDishes),
      scheduledTime: this.scheduledDate
    };


    this.orderService.scheduleOrder(body).
    subscribe(
        (response) => {
          console.log('Order submitted successfully', response);
        },
        error => {
          console.error('Error submitting order:', error);
        }
    );
  }

  fetchMenu(){
    this.orderService.fetchMenu().subscribe
    (
        (response: Dish[]) => {
          this.dishes = response;
          console.log(this.dishes)


        },
          error => {
          console.error('Error fetching permissions:', error);
        }
    );
  }

}
