import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

(window as any).global = window;

@Injectable()
export class OAuth2Service {

  accessToken: string;
  expiresAt: number;
  isLogined = false;

  constructor(public router: Router) {
  }

  public login(): void {
    this.router.navigate(['/login']);
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
}
