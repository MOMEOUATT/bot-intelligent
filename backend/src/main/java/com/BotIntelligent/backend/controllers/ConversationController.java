package com.BotIntelligent.backend.controllers;

import com.BotIntelligent.backend.dtos.ConversationDto;
import com.BotIntelligent.backend.entities.Conversation;
import com.BotIntelligent.backend.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@CrossOrigin("http://localhost:4200")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @PostMapping
    public ResponseEntity<Conversation> createConversation(@RequestBody ConversationDto dto) {
        try {
            Conversation conversation = conversationService.createConversation(dto.getUserId(), dto.getTitle());
            return ResponseEntity.status(HttpStatus.CREATED).body(conversation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conversation>> getUserConversations(@PathVariable Long userId) {
        try {
            List<Conversation> conversations = conversationService.getUserConversation(userId);
            return ResponseEntity.status(HttpStatus.OK).body(conversations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getByIdConversation(@PathVariable Long id) {
        return conversationService.getConversationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conversation> updateConversationTitle(@PathVariable Long id, @RequestBody ConversationDto dto) {
        try {
            Conversation updated = conversationService.updateConversationTitle(id, dto.getTitle());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id) {
        try {
            conversationService.deleteConversation(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countUserConversations(@PathVariable Long userId) {
        long count = conversationService.contUserConversation(userId);
        return ResponseEntity.ok(count);
    }
}
