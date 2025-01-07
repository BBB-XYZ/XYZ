import {ComponentFixture, TestBed} from '@angular/core/testing';
import {DashboardComponent} from './dashboard.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClient, provideHttpClient} from '@angular/common/http';
import {of} from 'rxjs';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent, BrowserAnimationsModule],
      providers: [
        provideHttpClient(),
        {
          provide: HttpClient,
          useValue: {
            get: () => of([{id: '1', name: 'Dashboard 1', owner: {}, widgets: []}]),
            put: () => of({id: '1', name: 'Dashboard 1', owner: {}, widgets: []}),
            post: () => of({id: '1', name: 'Dashboard 1', owner: {}, widgets: []})
          }
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
