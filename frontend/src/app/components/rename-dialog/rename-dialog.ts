import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
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
    MatInputModule
  ],
  templateUrl: './rename-dialog.html',
  styleUrl: './rename-dialog.css',
})
export class RenameDialog {

  newTitle: string = "";
  showError = false;

  constructor(
    public dialogRef: MatDialogRef<RenameDialog>,
    // @Inject(MAT_DIALOG_DATA) public data: RenameDialogData
  ){
    // this.newTitle = data.currentTitle;
  }

  isValid(): boolean {
    return this.newTitle.trim().length >= 3 && this.newTitle.trim().length <= 100;
  }

  onConfirm(): void {
    if(this.isValid()){
      this.dialogRef.close(this.newTitle.trim());
    } else {
      this.showError = true; 
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

}