import {Component, Inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-password-confirm-dialog',
  standalone: true,
  imports: [
    MatFormField,
    ReactiveFormsModule,
    MatDialogContent,
    MatDialogTitle,
    MatInput,
    MatDialogActions,
    MatButton,
    MatLabel,
  ],
  templateUrl: './password-confirm-dialog.component.html',
  styleUrl: './password-confirm-dialog.component.scss'
})
export class PasswordConfirmDialogComponent {
  protected passwordControl = new FormControl('', Validators.required);

  confirmForm = new FormGroup({
    confirmPassword: this.passwordControl,
  });

  constructor(
    private dialogRef: MatDialogRef<PasswordConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { username: string; password: string }
  ) {
  }

  get passwordsMatch(): boolean {
    return this.data.password === this.confirmForm.get('confirmPassword')?.value;
  }

  confirm(): void {
    this.dialogRef.close({
      confirmed: true,
      username: this.data.username,
      password: this.confirmForm.get('confirmPassword')?.value,
    });
  }

  cancel(): void {
    this.dialogRef.close({confirmed: false});
  }
}
