import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: '',
    // component: UsersComponent,
    loadChildren: './home/home.module#HomeModule'
  },
  {
    path: 'home',
    // component: UsersComponent,
    loadChildren: './home/home.module#HomeModule'
  },
  {
    path: 'login',
    // component: LoginComponent,
    loadChildren: './login/login.module#LoginModule'
  },
  {
    path: 'users',
    // component: LoginComponent,
    loadChildren: './user/users.module#UsersModule'
  },
  {
    path: 'users/:username',
    // component: LoginComponent,
    loadChildren: './user/form/user.module#UserModule'
  }
];

@NgModule({
  //imports: [RouterModule.forRoot(routes)],
  imports: [ RouterModule.forRoot(routes, {useHash:true} )],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
