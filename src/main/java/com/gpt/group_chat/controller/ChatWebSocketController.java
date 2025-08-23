package com.gpt.group_chat.controller;

import com.gpt.group_chat.model.Message;
import com.gpt.group_chat.repository.MessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate,
                                   MessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);

        Long groupId = message.getGroup().getId();
        messagingTemplate.convertAndSend("/topic/messages/" + groupId, message);
    }
}
