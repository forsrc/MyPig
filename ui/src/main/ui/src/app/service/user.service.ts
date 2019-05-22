import {Injectable} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Cookie} from 'ng2-cookies';
import {ToastrService} from 'ngx-toastr';
import {OAuth2Service} from "./oauth.service";
import {UrlService} from "./url.service";

(window as any).global = window;

@Injectable()
export class UserService {
  accessToken: string;

  constructor(public router: Router,
              public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public toastrService: ToastrService,
              public urlService: UrlService,
              public oauth2Service: OAuth2Service
  ) {
    this.accessToken = Cookie.get("access_token");
  }

  getUsers(callback:any) {

    this.httpClient.get(this.urlService.url.user, {headers : this.urlService.url.headers}).subscribe(
      data => {
        if (callback) {
          callback(data);
        }
      },
      error => {
        this.toastrService.error(error || 'Server error', "user faild");
      }
    );
  }

}
