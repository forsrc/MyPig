import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: '',
    // component: HomeComponent,
    loadChildren: './home/home.module#HomeModule'
  },
  {
    path: 'home',
    // component: HomeComponent,
    loadChildren: './home/home.module#HomeModule'
  },
  {
    path: 'login',
    // component: LoginComponent,
    loadChildren: './login/login.module#LoginModule'
  }
];

@NgModule({
  //imports: [RouterModule.forRoot(routes)],
  imports: [ RouterModule.forRoot(routes, {useHash:true} )],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
