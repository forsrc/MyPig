import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { FlexLayoutModule } from '@angular/flex-layout';
import {
  MatButtonModule,
  MatCheckboxModule,
  MatInputModule
} from '@angular/material';

import { LoginRoutingModule } from './login-dialog-routing.module';
import { LoginDialogComponent } from './login-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    FlexLayoutModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    LoginRoutingModule
  ],
  declarations: [LoginDialogComponent]
})
export class LoginDialogModule {}
