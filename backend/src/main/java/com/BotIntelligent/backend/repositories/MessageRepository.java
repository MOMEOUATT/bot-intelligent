package com.BotIntelligent.backend.repositories;

import com.BotIntelligent.backend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationId(Long conversationId);
    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
    List<Message> findByConversationIdAndIsBot(Long conversationId, Boolean isBot);
    long countByConversationId(Long conversationId);

    // Requête personnalisée avec @Query (SQL)
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId AND m.content LIKE %:keyword%")
    List<Message> searchMessagesByKeyword(@Param("conversationId") Long conversationId,
                                          @Param("keyword") String keyword);
}
