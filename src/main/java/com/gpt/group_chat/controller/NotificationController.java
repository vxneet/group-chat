package com.gpt.group_chat.controller;

import com.gpt.group_chat.model.Notification;
import com.gpt.group_chat.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository){
        this.notificationRepository=notificationRepository;
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotification(@PathVariable Long userId){
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId);
    }

    @GetMapping("/user/{userId}/unread")
    public List<Notification> getUnreadNotifications(@PathVariable Long userId){
        return notificationRepository.findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    @GetMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setRead(true);
            notificationRepository.save(notification);
            return ResponseEntity.ok(notification);
        }
        return ResponseEntity.badRequest().body("Notification not found");
    }
}
