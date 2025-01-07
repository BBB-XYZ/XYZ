import {Component} from '@angular/core';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import {ContentComponent} from './content/content.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatSelectModule, MatButtonModule, MatFormFieldModule, ContentComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent {
  protected dashboards: {name: string, id: string, widgets: string[]}[] = [
    {name: "dashboard1", id: "uuid-1",widgets:  ["w1", "w2"]},
    {name: "dashboard2", id: "uuid-2",widgets:  ["w1", "w2"]},
    {name: "dashboard3", id: "uuid-3",widgets:  ["w1", "w2"]},
    {name: "dashboard4", id: "uuid-4",widgets:  ["w1", "w2"]},
  ];

  protected selectedDashboardIndex = 0
  protected isEditing = false;

  toggleEditing() {
    this.isEditing = !this.isEditing;
    if (!this.isEditing) {
      this.cancel();
    }
  }

  createNewDashboard() {
    console.log("new dash")
  }

  cancel() {
    console.log("cancel")
  }


  save() {
    console.log("save")
  }
}
