import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BotAvatar } from './bot-avatar';

describe('BotAvatar', () => {
  let component: BotAvatar;
  let fixture: ComponentFixture<BotAvatar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BotAvatar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BotAvatar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
