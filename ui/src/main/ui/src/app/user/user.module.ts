import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {CdkTableModule} from '@angular/cdk/table';

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

import {UserRoutingModule} from './user-routing.module';
import {UserComponent} from './user.component';
import {UserService} from "../service/user.service";
import {UrlService} from "../service/url.service";


@NgModule({
  imports: [
    CdkTableModule,
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
    UserRoutingModule,
  ],
  providers:[UrlService, UserService],
  declarations: [UserComponent]
})
export class UserModule {
}
