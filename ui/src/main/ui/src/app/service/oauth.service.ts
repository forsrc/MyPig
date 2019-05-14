import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

(window as any).global = window;

@Injectable()
export class OAuth2Service {

  accessToken: string;
  expiresAt: number;
  isLogined = false;

  constructor(public router: Router, public httpClient: HttpClient) {
  }

  public login(): void {
    this.router.navigate(['#/login']);
    this.isLogined = true;
  }

  public logout(): void {

    this.accessToken = null;
    this.expiresAt = null;
    this.isLogined = false;
    this.router.navigate(['/']);
  }

  public isAuthenticated(): boolean {
    // return new Date().getTime() < this.expiresAt;
    return this.isLogined;
  }

  public getOauthToken(username: string, password: string): Observable<any> {
    const headers = new HttpHeaders()
      .set("Authorization", "Basic " + btoa("forsrc:forsrc"))
      .set("Content-type", "application/x-www-form-urlencoded; charset=UTF-8")
    ;
    //const url = 'http://mypig-sso-server:10000/sso/oauth/token';
    const url = 'http://mypig-ui:8888/oauth/token';
    const body = `grant_type=password&username=${username}&password=${password}`;
    return this.httpClient.post(url, body, {headers});
  }
}
