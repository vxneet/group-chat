package com.gpt.group_chat.repository;

import com.gpt.group_chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findByGroup_Id(Long groupId);
}
