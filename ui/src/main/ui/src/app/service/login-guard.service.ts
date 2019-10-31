import {CanActivate, Router} from '@angular/router';
import {Cookie} from "ng2-cookies";
import {OAuth2Service} from "./oauth.service";
import {Injectable} from "@angular/core";


@Injectable()
export class LoginGuard implements CanActivate {

  constructor(private router: Router, private oAuth2Service: OAuth2Service) {

  }

  canActivate() {
    //return true;
    if (!this.getAccessToken() || new Date().getTime() > this.oAuth2Service.expiresAt) {
      this.router.navigate(['/login'])
      return false
    }
    return true;
  }

  getAccessToken() {
    return Cookie.get("access_token");
  }
}
