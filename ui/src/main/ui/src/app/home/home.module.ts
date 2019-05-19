import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

import {FlexLayoutModule} from '@angular/flex-layout';
import {MatButtonModule, MatCheckboxModule, MatInputModule} from '@angular/material';

import {HomeRoutingModule} from './home-routing.module';
import {HomeComponent} from './home.component';

@NgModule({
  imports: [
    MatButtonModule,
    MatCheckboxModule,
    MatInputModule,
    CommonModule,
    FormsModule,
    FlexLayoutModule,
    HomeRoutingModule
  ],
  declarations: [HomeComponent]
})
export class HomeModule {
}
