import {Component, inject} from '@angular/core';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ContentComponent} from './content/content.component';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

export type Widget = {
  uuid: string;
  widgetStoreUrl: string;
  widgetId: string;
  x: number;
  y: number;
  width: number;
  height: number;
};

export type Dashboard = {
  id: string;
  name: string;
  owner: any;
  widgets: Widget[];
};

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatSelectModule, MatButtonModule, MatFormFieldModule, ContentComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent {
  protected dashboards: Dashboard[] = [];
  protected selectedDashboardIndex = 0;
  protected unmodifiedSelectedDashboard = this.dashboards[0];
  protected selectedDashboard = this.dashboards[0];
  protected isEditing = false;
  private httpClient = inject(HttpClient);
  private readonly API_URL = environment.apiUrl;

  public constructor() {
    this.httpClient.get<Dashboard[]>(`${this.API_URL}/dashboard/all`).subscribe({
      next: (response) => {
        for (const dashboard of response) {
          this.dashboards.push(
            dashboard,
          );
        }

        this.unmodifiedSelectedDashboard = {...this.dashboards[0]};
        this.selectedDashboard = this.dashboards[0];
      },
    });
  }

  protected toggleEditing() {
    this.isEditing = !this.isEditing;
    if (!this.isEditing) {
      this.cancel();
    }
  }

  protected createNewDashboard() {
    this.httpClient.put<Dashboard>(`${this.API_URL}/dashboard`, {}).subscribe(
      (response) => {
        this.dashboards.push(
          response
        )
      }
    )
  }

  protected updateDashboard(updatedDashboard: Dashboard): void {
    this.selectedDashboard = updatedDashboard;
  }

  protected cancel() {
    console.log(this.unmodifiedSelectedDashboard);
    console.log(this.dashboards[this.selectedDashboardIndex]);
    this.dashboards[this.selectedDashboardIndex] = this.unmodifiedSelectedDashboard;
    this.selectedDashboard = this.unmodifiedSelectedDashboard;
  }


  protected save() {
    this.unmodifiedSelectedDashboard = this.selectedDashboard;
    this.httpClient.post(`${this.API_URL}/dashboard`, this.selectedDashboard).subscribe();
    this.isEditing = false;
  }
}
