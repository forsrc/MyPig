import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatButtonModule, MatIconModule, MatMenuModule} from '@angular/material';
import {RouterModule} from '@angular/router';
import {NavbarComponent} from './navbar.component';


@NgModule({
  imports: [
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
    RouterModule,
    CommonModule
  ],
  exports: [NavbarComponent],
  declarations: [NavbarComponent],
})
export class NavBarModule {
}
