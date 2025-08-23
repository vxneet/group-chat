package com.gpt.group_chat.controller;

import com.gpt.group_chat.model.Group;
import com.gpt.group_chat.model.Message;
import com.gpt.group_chat.model.User;
import com.gpt.group_chat.repository.GroupRepository;
import com.gpt.group_chat.repository.MessageRepository;
import com.gpt.group_chat.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public MessageController(MessageRepository messageRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{groupId}/send/{userId}")
    public Message sendMessage(@PathVariable Long groupId, @PathVariable Long userId, @RequestBody String content) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Group> groupOpt = groupRepository.findById(groupId);

        if(userOpt.isPresent() && groupOpt.isPresent()){
            Message message = new Message();
            message.setContent(content);
            message.setSender(userOpt.get());
            message.setGroup(groupOpt.get());
            return messageRepository.save(message);
        }
        throw new RuntimeException("User not found");
    }

    @GetMapping("/{groupId}")
    public List<Message> findByGroup(@PathVariable Long groupId) {
        return  messageRepository.findByGroup_Id(groupId);
    }

}

