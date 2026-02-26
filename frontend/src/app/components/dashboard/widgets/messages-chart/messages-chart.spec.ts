import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessagesChart } from './messages-chart';

describe('MessagesChart', () => {
  let component: MessagesChart;
  let fixture: ComponentFixture<MessagesChart>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MessagesChart]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MessagesChart);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
