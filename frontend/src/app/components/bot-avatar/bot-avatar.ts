import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';

export type EyeState = 'open' | 'closed' | 'half';

@Component({
  selector: 'app-bot-avatar',
  imports: [CommonModule],
  templateUrl: './bot-avatar.html',
  styleUrl: './bot-avatar.css',
})
export class BotAvatar implements OnChanges {

  @Input() eyeState: EyeState = 'open';

  ngOnChanges(changes: SimpleChanges): void {
      if(changes['eyestate']){

      }
  }

  getEyeY(): number {
    switch(this.eyeState){
      case 'open':
        return 65;
      case 'half':
        return 68;
      case 'closed':
        return 70;
      default:
        return 65;
    }
  }

  /**
   * Largeur des yeux
   */
  getEyeWidth(): number {
    return this.eyeState === 'half' ? 10 : 12;
  }

  /**
   * Hauteur des yeux selon l'état
   */
  getEyeHeight(): number {
    switch (this.eyeState) {
      case 'open':
        return 14;
      case 'half':
        return 4;
      case 'closed':
        return 1;
      default:
        return 14;
    }
  }

}
