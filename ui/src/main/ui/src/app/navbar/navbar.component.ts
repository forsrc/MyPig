import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {OAuth2Service} from "../service/oauth.service";
import {Cookie} from "ng2-cookies";


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

  constructor(private router: Router, private oAuth2Service: OAuth2Service) {

  }

  logout() {
    this.oAuth2Service.expiresAt = 0;
    Cookie.delete("access_token");
    this.router.navigate(['/login']);
  }
}

