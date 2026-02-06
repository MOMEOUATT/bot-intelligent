package com.BotIntelligent.backend.repositories;

import com.BotIntelligent.backend.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByUserId(Long userId);
    List<Conversation> findByUserIdOrderByUpdatedAtDesc(Long userId);
    long countByUserId(Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}
