package com.BotIntelligent.backend.controllers;


import com.BotIntelligent.backend.service.ConversationService;
import com.BotIntelligent.backend.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin("http://localhost:4200")
public class StatsController {

    @Autowired
    private StatsService statsService;
    @Autowired
    private ConversationService conversationService;

    @GetMapping("/user/{userId}/dashboard")
    public ResponseEntity<Map<String, Object>> getUserDashboard(
            @PathVariable Long userId, @RequestParam(defaultValue ="7") int days) {

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("totalConversations", conversationService.contUserConversation(userId));
        dashboard.put("totalMessages", statsService.countUserMessages(userId));
        dashboard.put("likedMessages", statsService.countLikedMessages(userId));
        dashboard.put("dislikedMessages", statsService.countDislikedMessages(userId));

        dashboard.put("dailyStats", statsService.getDailyMessagesStats(userId, days));

        return ResponseEntity.ok(dashboard);

    }

    @GetMapping("/user/{userId}/messages/count")
    public ResponseEntity<Long> countUserMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(statsService.countUserMessages(userId));
    }

    @GetMapping("/user/{userId}/messages/liked/count")
    public ResponseEntity<Long> countUserLikedMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(statsService.countLikedMessages(userId));
    }

    @GetMapping("/user/{userId}/messages/disliked/count")
    public ResponseEntity<Long> countUseDislikedMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(statsService.countDislikedMessages(userId));
    }

    @GetMapping("/user/{userId}/messages/daily")
    public ResponseEntity<List<Map<String, Object>>> getDailyStats(
            @PathVariable Long userId, @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(statsService.getDailyMessagesStats(userId, days));
    }
}
