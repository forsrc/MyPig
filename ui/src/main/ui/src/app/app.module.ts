import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {
  MatButtonModule,
  MatCardModule,
  MatDialogModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatProgressBarModule,
  MatSelectModule,
  MatSidenavModule,
  MatTableModule,
  MatToolbarModule,
} from '@angular/material';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
import {FlexLayoutModule} from '@angular/flex-layout';

import {OAuth2Service} from './service/oauth.service';
import {NavBarModule} from './navbar/navbar.module';
import {FooterModule} from './footer/footer.module';
import {ServiceWorkerModule} from '@angular/service-worker';
import {environment} from '../environments/environment';
import {ToastrModule} from 'ngx-toastr';
import {UrlService} from "./service/url.service";


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    MatButtonModule,
    MatTableModule,
    MatDialogModule,
    MatInputModule,
    MatSelectModule,
    MatProgressBarModule,

    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FlexLayoutModule,
    ToastrModule.forRoot(),

    NavBarModule,
    FooterModule,
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
  ],
  providers: [UrlService, OAuth2Service],
  bootstrap: [AppComponent]
})
export class AppModule {
}
