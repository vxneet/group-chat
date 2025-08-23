package com.gpt.group_chat.repository;

import com.gpt.group_chat.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
