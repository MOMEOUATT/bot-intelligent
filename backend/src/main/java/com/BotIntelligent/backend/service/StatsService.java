package com.BotIntelligent.backend.service;

import com.BotIntelligent.backend.entities.Message;
import com.BotIntelligent.backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsService {

    @Autowired
    private MessageRepository messageRepository;

    public long countUserMessages(Long userId){
        return messageRepository.countByConversation_UserId(userId);
    }

    public long countLikedMessages(Long userId){
        return messageRepository.countByConversation_UserIdAndLikedTrue(userId);
    }

    public long countDislikedMessages(Long userId){
        return messageRepository.countByConversation_UserIdAndDislikedTrue(userId);
    }

    public List<Map<String, Object>> getDailyMessagesStats(Long userId, int days){
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        List<Message> messages = messageRepository.findByConversation_UserIdAndCreatedAtAfter(userId, startDate);

        Map<LocalDate, Long> messagesByDate = messages.stream()
                .collect(Collectors.groupingBy(
                        msg -> msg.getCreatedAt().toLocalDate(),
                        Collectors.counting()
                ));

        List<Map<String,Object>> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for(int i=days - 1; i>=0; i--){
            LocalDate date = LocalDate.now().minusDays(i);
            Map<String, Object> daysStats = new HashMap<>();
            daysStats.put("date", date.format(formatter));
            daysStats.put("messages", messagesByDate.getOrDefault(date, 0L));
            result.add(daysStats);
        }

        return result;
    }
}
