import { TestBed } from '@angular/core/testing';

import { ConversationEventService } from './conversation-event-service';

describe('ConversationEventService', () => {
  let service: ConversationEventService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConversationEventService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
