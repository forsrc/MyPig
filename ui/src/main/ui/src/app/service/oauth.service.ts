import {Injectable} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Cookie} from 'ng2-cookies';
import {ToastrService} from 'ngx-toastr';

(window as any).global = window;

@Injectable()
export class OAuth2Service {

  accessToken: string;
  expiresAt: number;
  isLogined = false;

  constructor(public router: Router,
              public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public toastrService: ToastrService
  ) {
  }

  public login(): void {
    this.router.navigate(['#/login']);
    this.isLogined = true;
  }

  public getParams(): Params {
    let params: Params;
    this.activatedRoute.queryParams.subscribe((p: Params) => {
      params = p;
    });
    return params;
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

  public getOauthToken(username: string, password: string, callback: any) {
    let isServer = this.getParams()["server"];
    console.log(this.getParams(), isServer);
    const headers = new HttpHeaders()
      .set("Authorization", "Basic " + btoa("forsrc:forsrc"))
      .set("Content-type", "application/x-www-form-urlencoded; charset=UTF-8")
    ;
    //const url = 'http://mypig-sso-server:10000/sso/oauth/token';
    const url = 'http://mypig-ui:8888/oauth/token';
    const body = `grant_type=password&username=${username}&password=${password}`;
    this.httpClient.post(url, body, {headers}).subscribe(
      (data) => {
        let token = data;
        this.toastrService.toastrConfig.positionClass = 'toast-bottom-right';
        this.toastrService.success(token['access_token'], "access token");
        this.saveToken(token);
        callback(data);
      },
      err => {
        console.error(err);
        this.toastrService.error(err.toString() || "Server error", "login faild");
      }
    );
  }

  public saveToken(token) {
    let expireDate = new Date().getTime() + (1000 * token.expires_in);
    Cookie.set("access_token", token.access_token, expireDate);
  }

  public retrieveToken(code: string, callback: any) {
    let params = new URLSearchParams();
    params.append('grant_type', 'authorization_code');
    params.append('client_id', 'ui');
    params.append('redirect_uri', 'http://localhost:4200');
    params.append('code', code);
    const url = 'http://mypig-ui:8888/oauth/token';
    let headers = new HttpHeaders(
      {
        'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
        'Authorization': 'Basic ' + btoa("forsrc:forsrc")
      });
    this.httpClient.post(url, params.toString(), {headers: headers}).subscribe(
      data => {
        this.toastrService.toastrConfig.positionClass = 'toast-bottom-right';
        this.toastrService.success(data['access_token'], "access token");
        this.saveToken(data);
        callback(data);
      },
      error => {
        this.toastrService.error(error.json().error || 'Server error', "login faild");
      }
    );

  }

  public getCode(): string {
    let i = window.location.href.indexOf('code');
    let code = null;
    if (i != -1) {
      code = window.location.href.substring(i + 5);
    }
    return code;
  }

  public toRetrieve() {
    window.location.href = 'http://mypig-ui:8888/oauth/authorize?response_type=code&client_id=ui&redirect_uri=http://localhost:4200';
  }

  public getResource(accessToken: string, resourceUrl: string, callback: any) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
      'Authorization': 'Bearer ' + accessToken
    });

    this.httpClient.get(resourceUrl, {headers: headers}).subscribe(
      data => {
        this.toastrService.toastrConfig.positionClass = 'toast-bottom-right';
        this.toastrService.success(data['access_token'], "access token");
        this.saveToken(data);
        callback(data);
      },
      error => {
        this.toastrService.error(error.json().error || 'Server error', "login faild");
      }
    );
  }

}
