import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenameDialog } from './rename-dialog';

describe('RenameDialog', () => {
  let component: RenameDialog;
  let fixture: ComponentFixture<RenameDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RenameDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RenameDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
