package com.gpt.group_chat.repository;

import com.gpt.group_chat.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    List<Notification> findByGroupIdOrderByCreatedAtDesc(Long groupId);
    long countByRecipientIdAndIsReadFalse(Long userId);
}
