import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WaveformLogo } from './waveform-logo';

describe('WaveformLogo', () => {
  let component: WaveformLogo;
  let fixture: ComponentFixture<WaveformLogo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WaveformLogo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WaveformLogo);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
