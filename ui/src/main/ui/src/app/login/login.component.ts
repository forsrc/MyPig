import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {OAuth2Service} from '../service/oauth.service'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  email = 'forsrc@gmail.com';
  password = 'forsrc';

  constructor(private router: Router, private oAuth2Service: OAuth2Service) {
  }

  ngOnInit() {
  }

  onLogin() {
    localStorage.setItem('isLoggedin', 'true');
    this.oAuth2Service.getOauthToken(this.email, this.password).subscribe(
      data => {
        console.log(data);
        this.oAuth2Service.accessToken = data.access_token;
        this.router.navigate(['home']);
      },
      error => {
        console.error(error)
      }
    )


  }
}
