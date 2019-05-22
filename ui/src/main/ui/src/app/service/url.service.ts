import {Injectable} from '@angular/core';
import {HttpHeaders} from "@angular/common/http";
import {Cookie} from "ng2-cookies";

@Injectable()
export class UrlService {
  host = "http://mypig-ui:8888";

  url() {
    return {
      user: `${this.host}/sso/api/v1/user/?access_token=${this.getAccessToken()}`,
      oauthToken: `${this.host}/sso/oauth/token`,
      headers: new HttpHeaders({
        'Content-type': 'application/json; charset=utf-8',
        'Authorization': `Bearer ${this.getAccessToken()}`
      })
    }
  };

  getAccessToken(): string {
    return Cookie.get("access_token");
  }

  constructor() {
  }
}
