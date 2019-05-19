import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.scss']
})
export class LoginDialogComponent implements OnInit {

  public event: EventEmitter<any> = new EventEmitter();

  userInfo = {
    username: '',
    passowrd: '',
  };

  constructor(private router: Router,
              public dialogRef: MatDialogRef<LoginDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public title: any) {
  }

  ngOnInit() {
  }

  onLogin() {
    localStorage.setItem('isLoggedin', 'true');
    this.router.navigate(['/home']);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    this.event.emit({data: this.userInfo});
    this.dialogRef.close();
  }
}
