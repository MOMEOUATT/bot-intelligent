package com.BotIntelligent.backend.controllers;

import com.BotIntelligent.backend.dtos.MessageDto;
import com.BotIntelligent.backend.entities.Message;
import com.BotIntelligent.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin("http://localhost:4200")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // ✅ ENDPOINT UNIFIÉ - gère texte ET fichiers
    @PostMapping
    public ResponseEntity<List<Message>> sendMessage(@RequestBody Map<String, Object> payload) {
        try {
            Long conversationId = Long.valueOf(payload.get("conversationId").toString());
            String content = payload.get("content") != null ? payload.get("content").toString() : "";
            String fileUrl = payload.get("fileUrl") != null ? payload.get("fileUrl").toString() : null;
            String fileName = payload.get("fileName") != null ? payload.get("fileName").toString() : null;

            System.out.println("=== BACKEND RECEIVE ===");
            System.out.println("ConversationId: " + conversationId);
            System.out.println("Content: " + content);
            System.out.println("FileUrl: " + fileUrl);
            System.out.println("FileName: " + fileName);

            List<Message> messages = messageService.sendMessage(conversationId, content, fileUrl, fileName);

            System.out.println("=== BACKEND RESPONSE ===");
            System.out.println("Messages count: " + messages.size());
            messages.forEach(m -> {
                System.out.println("- Message id=" + m.getId() + " isBot=" + m.getIsBot() + " fileUrl=" + m.getFileUrl());
            });

            // Broadcaster via WebSocket
            for (Message msg : messages) {
                messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, msg);
            }

            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
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

    @PutMapping("/{id}/like")
    public ResponseEntity<Message> likeMessage(@PathVariable Long id){
        try {
            Message message = messageService.likeMessage(id);
            return ResponseEntity.ok(message);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/dislike")
    public ResponseEntity<Message> dislikeMessage(@PathVariable Long id){
        try {
            Message message = messageService.dislikeMessage(id);
            return ResponseEntity.ok(message);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}/feedback")
    public ResponseEntity<Message> removeFeedback(@PathVariable Long id){
        try {
            Message message = messageService.removeFeedback(id);
            return ResponseEntity.ok(message);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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