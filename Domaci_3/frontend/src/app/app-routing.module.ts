import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {CreateUsersComponent} from "./components/users/create-users/create-users.component";
import {TableUsersComponent} from "./components/users/table-users/table-users.component";
import {EditUsersComponent} from "./components/users/edit-users/edit-users.component";
import {AuthGuard} from "./auth.guard";
import {ErrorComponent} from "./components/orders/error/error.component";
import {OrderComponent} from "./components/orders/order/order.component";
import {SearchComponent} from "./components/orders/search/search.component";

const routes: Routes = [
  {
    path: "",
    component: HomeComponent,
  },
  {
    path: "login",
    component: LoginComponent,
  },
  {
    path: "table-users",
    component: TableUsersComponent,
    canActivate: [AuthGuard],
    canDeactivate: [AuthGuard]
  },
  {
    path: "create-user",
    component: CreateUsersComponent,
    canActivate: [AuthGuard],
    canDeactivate: [AuthGuard]
  },
  {
    path: 'edit-user/:id',
    component: EditUsersComponent,
    canActivate: [AuthGuard],
    canDeactivate: [AuthGuard]
  },
  {
    path: 'error',
    component: ErrorComponent,
    canActivate: [AuthGuard],
    canDeactivate: [AuthGuard]
  },
  {
    path: 'order',
    component: OrderComponent,
    canActivate: [AuthGuard],
    canDeactivate: [AuthGuard]
  },
  {
    path: 'search',
    component: SearchComponent,
    canActivate: [AuthGuard],
    canDeactivate: [AuthGuard]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
