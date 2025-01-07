import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-content',
  standalone: true,
  templateUrl: './content.component.html',
  styleUrl: './content.component.scss'
})

export class ContentComponent {
  @Input() dashboardData: {name: string, id: string, widgets: string[]} = {name: "", id: "", widgets: []};
  @Input() edit: boolean = false;
}
