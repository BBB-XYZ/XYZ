import {Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {loggedInGuard} from './auth/logged-in.guard';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {DashboardComponent} from './Dashboard/dashboard.component';

export const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: '/home'},
  {path: 'home', component: HomeComponent, canActivate: [loggedInGuard]},
  {path: 'dashboard', pathMatch: 'full', redirectTo: '/dashboards'},
  {path: 'dashboards', component: DashboardComponent, canActivate: [loggedInGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: LoginComponent},
  {path: '**', component: PageNotFoundComponent},
];
