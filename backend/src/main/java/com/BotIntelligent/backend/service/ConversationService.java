package com.BotIntelligent.backend.service;

import com.BotIntelligent.backend.entities.Conversation;
import com.BotIntelligent.backend.entities.User;
import com.BotIntelligent.backend.repository.ConversationRepository;
import com.BotIntelligent.backend.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    public Conversation createConversation(Long userId, String title){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Conversation conversation = new Conversation();
        conversation.setUser(user);
        conversation.setTitle(title != null ? title: "Nouvelle conversation");
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setUpdatedAt(LocalDateTime.now());

        return conversationRepository.save(conversation);
    }

    public List<Conversation> getUserConversation(Long userId){
        return conversationRepository.findByUserIdOrderByUpdatedAtDesc(userId);
    }

    public Optional<Conversation> getConversationById(Long id){
        return conversationRepository.findById(id);
    }

    public Conversation updateConversationTitle(Long id, String title){
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

        conversation.setTitle(title);
        conversation.setUpdatedAt(LocalDateTime.now());

        return conversationRepository.save(conversation);
    }

    public Conversation updateConversationTimestamp(Long id){
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

        conversation.setUpdatedAt(LocalDateTime.now());

        return conversationRepository.save(conversation);
    }

    public void deleteConversation(Long id){
        conversationRepository.deleteById(id);
    }

    public Long contUserConversation(Long userId){
        return conversationRepository.countByUserId(userId);
    }

}
