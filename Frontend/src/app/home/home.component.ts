import {Component, inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MatButtonModule} from '@angular/material/button';
import {CommonModule} from '@angular/common';
import {environment} from '../../environments/environment';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MatButtonModule, CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  protected responseValue = '';
  protected statusCode = 0;
  private readonly apiUrl = environment.apiUrl;
  private http = inject(HttpClient);

  protected triggerRequest() {
    this.http.get(`${this.apiUrl}/test`, {observe: 'response'}).subscribe({
      next: (response) => {
        this.responseValue = JSON.stringify(response.body);
        this.statusCode = response.status;
      },
      error: (error) => {
        this.responseValue = error.message;
        this.statusCode = error.status;
      }
    });
  }
}
