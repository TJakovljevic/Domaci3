import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './components/app/app.component';
import { LoginComponent } from './components/login/login.component';
import { TableUsersComponent } from './components/users/table-users/table-users.component';
import { CreateUsersComponent } from './components/users/create-users/create-users.component';
import { EditUsersComponent } from './components/users/edit-users/edit-users.component';
import {FormsModule} from "@angular/forms";
import { HomeComponent } from './components/home/home.component';
import {HttpClientModule} from "@angular/common/http";
import { SearchComponent } from './components/orders/search/search.component';
import { OrderComponent } from './components/orders/order/order.component';
import { ErrorComponent } from './components/orders/error/error.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    TableUsersComponent,
    CreateUsersComponent,
    EditUsersComponent,
    HomeComponent,
    SearchComponent,
    OrderComponent,
    ErrorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
