package com.gpt.group_chat.controller;

import com.gpt.group_chat.model.Group;
import com.gpt.group_chat.model.User;
import com.gpt.group_chat.repository.GroupRepository;
import com.gpt.group_chat.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("groups")
public class GroupController {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    GroupController(GroupRepository groupRepository,  UserRepository userRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupRepository.save(group);
    }

    @GetMapping
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @PostMapping("/{groupId}/addUser/{userId}")
    public Group insertUserToGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        Optional<User> userOpt = userRepository.findById(userId);

        if(groupOpt.isPresent() && userOpt.isPresent()) {
            Group group = groupOpt.get();
            User user = userOpt.get();

            group.getMembers().add(user);
            return groupRepository.save(group);
        }
        throw new RuntimeException("User not found");
    }
}
