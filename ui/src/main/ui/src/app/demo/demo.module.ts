import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

import {FlexLayoutModule} from '@angular/flex-layout';
import {MatButtonModule, MatCheckboxModule, MatInputModule} from '@angular/material';

import {DemoRoutingModule} from './demo-routing.module';
import {DemoComponent} from './demo.component';

@NgModule({
  imports: [
    MatButtonModule,
    MatCheckboxModule,
    MatInputModule,
    CommonModule,
    FormsModule,
    FlexLayoutModule,
    DemoRoutingModule
  ],
  declarations: [DemoComponent]
})
export class DemoModule {
}
