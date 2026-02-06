package com.BotIntelligent.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long conversationId;
    private String content;
    private Boolean isBot;
}
