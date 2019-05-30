import {Injectable} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Cookie} from 'ng2-cookies';
import {ToastrService} from 'ngx-toastr';
import {UrlService} from "./url.service";

(window as any).global = window;

@Injectable()
export class OAuth2Service {

  accessToken: string;
  expiresAt: number;
  isLogined = false;

  constructor(public router: Router,
              public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public toastrService: ToastrService,
              public urlService: UrlService,
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
    const body = `grant_type=password&username=${username}&password=${password}`;
    this.httpClient.post(this.urlService.url().oauthToken, body, {headers: this.urlService.url().oauthTokenHeaders})
      .subscribe(
      (data) => {
        let token:any = data;
        this.toastrService.toastrConfig.positionClass = 'toast-bottom-right';
        this.toastrService.success(token.expires_in, "access token");
        this.saveToken(token);
        callback(data);
      },
      (err: any) => {
        console.error(err);
        if (typeof err == "object") {
          if (err.error["error_description"]) {
            this.toastrService.error(err.error["error_description"], "Login failed");
            return;
          }
          this.toastrService.error(JSON.stringify(err), "Login failed");
          return;
        }
        this.toastrService.error(err || "Server error", "Login failed");
      }
    );
  }

  public saveToken(token: any) {
    let expireDate: Date = new Date(new Date().getTime() + (1000 * token.expires_in));
    this.expiresAt = expireDate.getTime();
    Cookie.set("access_token", token.access_token, expireDate);
    console.info(Cookie.getAll());
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
