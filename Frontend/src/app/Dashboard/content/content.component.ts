import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-content',
  standalone: true,
  imports: [],
  templateUrl: './content.component.html',
  styleUrl: './content.component.scss'
})

export class ContentComponent {
  @Input() dashboardData!: {name: string, id: string, widgets: string[]};
  @Input() edit!: boolean;
}
