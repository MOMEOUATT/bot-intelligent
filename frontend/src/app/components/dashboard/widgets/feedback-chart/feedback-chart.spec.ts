import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedbackChart } from './feedback-chart';

describe('FeedbackChart', () => {
  let component: FeedbackChart;
  let fixture: ComponentFixture<FeedbackChart>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedbackChart]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeedbackChart);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
