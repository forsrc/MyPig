import {Injectable} from '@angular/core';
import {HttpHeaders} from "@angular/common/http";
import {Cookie} from "ng2-cookies";

@Injectable()
export class UrlService {
  host = "https://mypig-ui:8888";
  url() {
    return {
      users: `${this.host}/sso/api/v1/user/?access_token=${this.getAccessToken()}`,
      user: (username) => `${this.host}/sso/api/v1/user/${username}?access_token=${this.getAccessToken()}`,
      oauthToken: `${this.host}/sso/oauth/token`,
      headers: new HttpHeaders({
        'Content-type': 'application/json; charset=utf-8',
        'Authorization': `Bearer ${this.getAccessToken()}`
      }),
      oauthTokenHeaders: new HttpHeaders({
        "Authorization": "Basic " + btoa("forsrc:forsrc"),
        "Content-type": "application/x-www-form-urlencoded; charset=UTF-8"
      }),
    }
  };

  getAccessToken(): string {
    return Cookie.get("access_token");
  }

  constructor() {
  }
}
