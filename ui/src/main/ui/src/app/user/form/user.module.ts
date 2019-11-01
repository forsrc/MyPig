import {NgModule} from '@angular/core';
import {CommonModule, Location} from '@angular/common';
import {FormsModule} from '@angular/forms';


import {FlexLayoutModule} from '@angular/flex-layout';
import {
  MatButtonModule,
  MatCheckboxModule,
  MatIconModule,
  MatInputModule,
  MatSidenavModule,
  MatToolbarModule,
  MatTreeModule,
  MatRadioModule,
  MatSelectModule,
} from '@angular/material';

import {UserRoutingModule} from './user-routing.module';
import {UserComponent} from './user.component';
import {UrlService} from "../../service/url.service";
import {UserService} from "../../service/user.service";

@NgModule({
  imports: [
    MatButtonModule,
    MatCheckboxModule,
    MatInputModule,
    MatToolbarModule,
    MatSidenavModule,
    MatIconModule,
    MatTreeModule,
    MatRadioModule,
    MatSelectModule,
    CommonModule,
    FormsModule,
    FlexLayoutModule,
    UserRoutingModule
  ],
  providers: [Location, UrlService, UserService],
  declarations: [UserComponent]
})
export class UserModule {
}
