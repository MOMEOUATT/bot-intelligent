package com.BotIntelligent.backend.controllers;

import com.BotIntelligent.backend.dtos.MessageDto;
import com.BotIntelligent.backend.entities.Message;
import com.BotIntelligent.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin("http://localhost:4200")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message[]> sendMessage(@RequestBody MessageDto dto) {
        try {
            Message userMessage = messageService.sendUserMessage(dto.getConversationId(), dto.getContent());

            Message botMessage = messageService.sendBotResponse(dto.getConversationId(), dto.getContent());

            Message[] messages = {userMessage, botMessage};
            return ResponseEntity.status(HttpStatus.CREATED).body(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Message>> getConversationMessages(@PathVariable Long conversationId){
        try {
            List<Message> messages = messageService.getConversationMessages(conversationId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id){
        return messageService.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        try {
            messageService.deleteMessage(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/conversation/{conversationId}/search")
    public ResponseEntity<List<Message>> searchMessage(@PathVariable Long conversationId, @RequestParam String keyword){
        try {
            List<Message> messages = messageService.searchMessage(conversationId, keyword);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/conversation/{conversationId}/count")
    public ResponseEntity<Long> countConversationMessages(@PathVariable Long conversationId){
        long count = messageService.countConversationMessages(conversationId);
        return ResponseEntity.ok(count);
    }
}
