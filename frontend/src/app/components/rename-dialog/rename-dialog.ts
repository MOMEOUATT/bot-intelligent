import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

export interface RenameDialogData {
  currentTitle: string;
}

@Component({
  selector: 'app-rename-dialog',
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule
  ],
  templateUrl: './rename-dialog.html',
  styleUrl: './rename-dialog.css',
})
export class RenameDialog {

  newTitle = '';
  showError = false;

  constructor(
    public dialogRef: MatDialogRef<RenameDialog>
  ) {}

  isValid(): boolean {
    return this.newTitle.trim().length >= 3 && 
           this.newTitle.trim().length <= 100;
  }

  onConfirm(): void {
    if (this.isValid()) {
      this.dialogRef.close(this.newTitle.trim());
    } else {
      this.showError = true;
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

}