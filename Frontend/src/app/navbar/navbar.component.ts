import {Component, inject} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../auth/auth.service';
import {MatIconModule} from '@angular/material/icon';
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatIconModule, MatButton],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  get isAuthenticated() {
    return this.authService.isAuthenticated();
  }

  toggleAuth() {
    if (this.isAuthenticated) {
      this.authService.logout();
    }

    this.router.navigate(['/login']).then();
  }

  navigateTo(route: string) {
    this.router.navigate([route]).then();
  }

}
