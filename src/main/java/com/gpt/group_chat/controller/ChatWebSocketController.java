package com.gpt.group_chat.controller;

import com.gpt.group_chat.model.Message;
import com.gpt.group_chat.repository.MessageRepository;
import com.gpt.group_chat.service.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final NotificationService notificationService;

    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate,
                                   MessageRepository messageRepository, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.notificationService = notificationService;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage= messageRepository.save(message);

        Long groupId = message.getGroup().getId();
        messagingTemplate.convertAndSend("/topic/messages/" + groupId, savedMessage);

        // Send notification to other group members
        notificationService.notifyGroupMessage(savedMessage);
    }

}
