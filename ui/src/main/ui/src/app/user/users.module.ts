import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

import {FlexLayoutModule} from '@angular/flex-layout';
import {
  MatButtonModule,
  MatCheckboxModule,
  MatInputModule,
  MatPaginatorModule,
  MatSelectModule,
  MatSortModule,
  MatTableModule,
} from '@angular/material';

import {UsersRoutingModule} from './users-routing.module';
import {UsersComponent} from './users.component';
import {UserService} from "../service/user.service";
import {UrlService} from "../service/url.service";


@NgModule({
  imports: [
    MatButtonModule,
    MatCheckboxModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatCheckboxModule,
    MatSelectModule,
    CommonModule,
    FormsModule,
    FlexLayoutModule,
    UsersRoutingModule,
  ],
  providers: [UrlService, UserService],
  declarations: [UsersComponent]
})
export class UsersModule {
}
