import {Injectable} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from "@angular/common/http";
import {Cookie} from 'ng2-cookies';
import {ToastrService} from 'ngx-toastr';
import {OAuth2Service} from "./oauth.service";
import {UrlService} from "./url.service";
import {Observable} from "rxjs";
import {catchError, tap} from 'rxjs/operators';

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

  getUsers(callback: any) {

    this.httpClient.get(this.urlService.url().users, {headers: this.urlService.url().headers}).subscribe(
      data => {
        if (callback) {
          callback(data);
        }
      },
      error => {
        this.toastrService.error(error || 'Server error', "user failed");
      }
    );
  }

  getUser(username: string): Observable<any> {

    return this.httpClient.get(this.urlService.url().user(username), {headers: this.urlService.url().headers})
      .pipe(
        tap(_ => console.log(`getUser ${JSON.stringify(_)}`)),
        catchError((err: any, caught: Observable<any>) => {
          console.info(<any>`getUser: username=${username}, ${JSON.stringify(err)}`);
          return null;
        })
      );
  }
}
