import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-waveform-logo',
  imports: [CommonModule],
  templateUrl: './waveform-logo.html',
  styleUrl: './waveform-logo.css',
})
export class WaveformLogo {

  @Input() size: 'small' | 'medium' | 'large' = 'medium';

  dims: Record<string, number> = {
    small:  24,
    medium: 36,
    large:  56
  };

}
