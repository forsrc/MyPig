import {AfterContentInit, Component, OnInit} from '@angular/core';
import {OAuth2Service} from './service/oauth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterContentInit {
  title = 'mypig-ui';
  isLoading = true;

  constructor(public oauth2: OAuth2Service) {
  }

  ngOnInit(): void {

  }

  ngAfterContentInit(): void {
    setTimeout(() => {
      this.isLoading = false;
    }, 2000);

  }
}
