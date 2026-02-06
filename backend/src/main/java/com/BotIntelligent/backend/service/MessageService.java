package com.BotIntelligent.backend.service;


import com.BotIntelligent.backend.entities.Conversation;
import com.BotIntelligent.backend.entities.Message;
import com.BotIntelligent.backend.repositories.ConversationRepository;
import com.BotIntelligent.backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationService conversationService;

    @Autowired
//    @Lazy
    private BotService botService;

    public Message sendUserMessage(Long conversationId, String content){
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

        Message userMessage = new Message();
        userMessage.setConversation(conversation);
        userMessage.setContent(content);
        userMessage.setIsBot(false);
        userMessage.setCreatedAt(LocalDateTime.now());

        Message savedUserMessage = messageRepository.save(userMessage);

        conversationService.updateConversationTimestamp(conversationId);

        return savedUserMessage;
    }

    public Message sendBotResponse(Long conversationId, String content){
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

        String botResponse = botService.generateResponse(content);

        Message botMessage = new Message();
        botMessage.setConversation(conversation);
        botMessage.setContent(botResponse);
        botMessage.setIsBot(true);
        botMessage.setCreatedAt(LocalDateTime.now());

        Message savedBotMessage = messageRepository.save(botMessage);

        conversationService.updateConversationTimestamp(conversationId);

        return savedBotMessage;
    }

    public List<Message> getConversationMessages(Long conversationId){
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    public Optional<Message> getMessageById(Long id){
        return messageRepository.findById(id);
    }

    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
    }

    public long countConversationMessages(Long conversationId){
        return messageRepository.countByConversationId(conversationId);
    }

    public List<Message> searchMessage(Long conversationId, String keyword){
        return messageRepository.searchMessagesByKeyword(conversationId, keyword);
    }
}
