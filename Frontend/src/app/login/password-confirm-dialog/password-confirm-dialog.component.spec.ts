import {ComponentFixture, TestBed} from '@angular/core/testing';
import {PasswordConfirmDialogComponent} from './password-confirm-dialog.component';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

describe('PasswordConfirmDialogComponent', () => {
  let component: PasswordConfirmDialogComponent;
  let fixture: ComponentFixture<PasswordConfirmDialogComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        PasswordConfirmDialogComponent,
        BrowserAnimationsModule,
      ],
      providers: [
        {
          provide: MatDialogRef,
          useValue: {},
        },
        {
          provide: MAT_DIALOG_DATA,
          useValue: {},
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PasswordConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
