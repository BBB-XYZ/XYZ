import {AfterViewInit, Component, EventEmitter, Input, Output, SimpleChanges, ViewChild} from '@angular/core';
import {GridStack, GridStackOptions, GridStackWidget} from 'gridstack';
import {GridstackComponent} from 'gridstack/dist/angular';
import {FormsModule} from '@angular/forms';
import {Dashboard, Widget} from '../dashboard.component';

@Component({
  selector: 'app-content',
  standalone: true,
  imports: [
    GridstackComponent,
    FormsModule,
  ],
  templateUrl: './content.component.html',
  styleUrl: './content.component.scss'
})

export class ContentComponent implements AfterViewInit {
  @Input() dashboardData: Dashboard = {} as Dashboard;
  @Input() edit: boolean = false;
  @Output() dashboardEdit = new EventEmitter<Dashboard>();

  @ViewChild(GridStack) grid?: GridStack;

  private gridOptions: GridStackOptions = {
    margin: 5,
    minRow: 1,
    acceptWidgets: true,
  };

  private dashboard: GridStackWidget[] = [];

  ngAfterViewInit(): void {
    this.initializeGrid();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['edit']) {
      this.updateGridEditStatus(this.edit);
    }

    if (changes['dashboardData']) {
      this.updateGridFromDashboardData();
    }
  }

  onDashboardEdit(): void {
    this.dashboardEdit.emit(this.dashboardData);
  }

  private initializeGrid(): void {
    this.grid = GridStack.init(this.gridOptions);
    if (this.dashboardData && this.dashboardData.widgets) {
      this.dashboard = this.mapWidgetsToGridStackWidgets(this.dashboardData.widgets);
    }

    if (this.grid) {
      this.grid.load(this.dashboard);
      this.updateGridEditStatus(this.edit);

      this.grid.on('change', (event, items) => {
        this.updateDashboardData(items as GridStackWidget[]);
      });
    }
  }

  private updateGridEditStatus(edit: boolean): void {
    if (this.grid) {
      if (edit) {
        this.grid.enable();
      } else {
        this.grid.disable();
      }
    }
  }

  private mapWidgetsToGridStackWidgets(widgets: Widget[]): GridStackWidget[] {
    return widgets.map(widget => ({
      x: widget.x,
      y: widget.y,
      width: widget.width,
      height: widget.height,
      id: widget.uuid,
      content: `Widget: ${widget.widgetId}`,
    }));
  }

  private updateDashboardData(items: GridStackWidget[]): void {
    if (this.dashboardData && this.dashboardData.widgets) {
      this.dashboardData.widgets = items.map(item => ({
        uuid: item.id!,
        widgetStoreUrl: this.dashboardData.widgets.find(w => w.uuid === item.id)?.widgetStoreUrl || '',
        widgetId: this.dashboardData.widgets.find(w => w.uuid === item.id)?.widgetId || '',
        x: item.x!,
        y: item.y!,
        width: item.w!,
        height: item.h!,
      }));

      this.dashboardEdit.emit(this.dashboardData);
    }
  }

  private updateGridFromDashboardData(): void {
    if (this.dashboardData && this.dashboardData.widgets) {
      this.dashboard = this.mapWidgetsToGridStackWidgets(this.dashboardData.widgets);
      if (this.grid) {
        this.grid.removeAll();
        this.grid.load(this.dashboard);
      }
    }
  }
}
