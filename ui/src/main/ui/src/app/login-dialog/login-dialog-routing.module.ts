import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginDialogComponent} from './login-dialog.component';

const routes: Routes = [
  {
    path: '/',
    component: LoginDialogComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LoginRoutingModule {
}
