import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';
import {PasswordConfirmDialogComponent} from "./password-confirm-dialog/password-confirm-dialog.component";
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {MatToolbar} from "@angular/material/toolbar";
import {Router} from "@angular/router";
import {AuthService} from "../auth/auth.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatCard,
    MatCardContent,
    MatFormField,
    MatInput,
    MatButton,
    MatLabel,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  protected usernameControl = new FormControl('', Validators.required);
  protected passwordControl = new FormControl('', Validators.required);

  protected loginForm = new FormGroup({
    username: this.usernameControl,
    password: this.passwordControl,
  });

  private dialog = inject(MatDialog);
  private router = inject(Router);
  private authService = inject(AuthService);

  register(): void {
    if (this.loginForm.invalid) return;

    const dialogRef = this.dialog.open(PasswordConfirmDialogComponent, {
      data: {
        username: this.usernameControl.value,
        password: this.passwordControl.value,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result?.confirmed) {
        this.authService.register(result.username, result.password).then((success) => {
          if (success) {
            this.router.navigate(['/home']).then();
          } else {
            console.error('Login failed');
            this.router.navigate(['/login']).then();
          }
        });
      }
    });
  }

  login(): void {
    const username = this.usernameControl.value ?? '';
    const password = this.passwordControl.value ?? '';

    this.authService.login(username, password).then((success) => {
      if (success) {
        this.router.navigate(['/home']).then();
      } else {
        console.error('Login failed');
        this.router.navigate(['/login']).then();
      }
    });
  }
}
