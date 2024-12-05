import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {CreateUsersComponent} from "./components/users/create-users/create-users.component";
import {TableUsersComponent} from "./components/users/table-users/table-users.component";
import {EditUsersComponent} from "./components/users/edit-users/edit-users.component";
import {AuthGuard} from "./auth.guard";

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
  { path: 'edit-user/:id',
    component: EditUsersComponent,
    canActivate: [AuthGuard],
    canDeactivate: [AuthGuard]
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
