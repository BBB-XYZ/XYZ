import {Component, inject} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../auth/auth.service';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatButton],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  protected isAuthenticated() {
    return this.authService.isAuthenticated();
  }

  protected toggleAuth() {
    if (this.isAuthenticated()) {
      this.authService.logout();
    }

    this.navigateTo('/login');
  }

  protected navigateTo(route: string) {
    this.router.navigate([route]).then();
  }

}
