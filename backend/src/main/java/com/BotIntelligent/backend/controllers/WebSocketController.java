package com.BotIntelligent.backend.controllers;

import com.BotIntelligent.backend.dtos.MessageDto;
import com.BotIntelligent.backend.entities.Message;
import com.BotIntelligent.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(MessageDto messageDto) {
        try {
            Message userMessage = messageService.sendUserMessage(messageDto.getConversationId(), messageDto.getContent());
            messagingTemplate.convertAndSend("/topic/messages" +  messageDto.getConversationId(), userMessage);

            Message botMessage = messageService.sendBotResponse(messageDto.getConversationId(), messageDto.getContent());
            messagingTemplate.convertAndSend("/topic/messages" +  messageDto.getConversationId(), botMessage);
        } catch (Exception e) {
            MessageDto errorMessage = new MessageDto();
            errorMessage.setConversationId(messageDto.getConversationId());
            errorMessage.setContent("Erreur lors de l'envoi du message" + e.getMessage());
            errorMessage.setIsBot(true);
            messagingTemplate.convertAndSend("/topic/messages" +  messageDto.getConversationId(), errorMessage);
        }
    }

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public MessageDto userTyping(MessageDto messageDto) {
        return messageDto;
    }
}
