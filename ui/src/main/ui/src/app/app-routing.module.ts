import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginGuard} from "./service/login-guard.service";
import {OAuth2Service} from "./service/oauth.service";

const routes: Routes = [
  {
    path: '',
    // component: UsersComponent,
    loadChildren: './login/login.module#LoginModule'
  },
  {
    path: 'home',
    // component: UsersComponent,
    loadChildren: './home/home.module#HomeModule',
    canActivate:[LoginGuard]
  },
  {
    path: 'login',
    // component: LoginComponent,
    loadChildren: './login/login.module#LoginModule'
  },
  {
    path: 'users',
    // component: LoginComponent,
    loadChildren: './user/users.module#UsersModule',
    canActivate:[LoginGuard]
  },
  {
    path: 'users/:username',
    // component: LoginComponent,
    loadChildren: './user/form/user.module#UserModule',
    canActivate:[LoginGuard]
  }
];

@NgModule({
  //imports: [RouterModule.forRoot(routes)],
  imports: [ RouterModule.forRoot(routes, {useHash:true} )],
  exports: [RouterModule],
  providers: [OAuth2Service, LoginGuard]
})
export class AppRoutingModule {
}
