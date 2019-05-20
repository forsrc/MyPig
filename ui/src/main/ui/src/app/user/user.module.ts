import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {CdkTableModule} from '@angular/cdk/table';

import {FlexLayoutModule} from '@angular/flex-layout';
import {
  MatButtonModule,
  MatCheckboxModule,
  MatInputModule,
  MatTableModule,
  MatPaginatorModule,
  MatSortModule,
} from '@angular/material';

import {UserRoutingModule} from './user-routing.module';
import {UserComponent} from './user.component';


@NgModule({
  imports: [
    CdkTableModule,
    MatButtonModule,
    MatCheckboxModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    CommonModule,
    FormsModule,
    FlexLayoutModule,
    UserRoutingModule,
  ],
  declarations: [UserComponent]
})
export class UserModule {
}
