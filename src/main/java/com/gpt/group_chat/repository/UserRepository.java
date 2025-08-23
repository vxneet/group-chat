package com.gpt.group_chat.repository;

import com.gpt.group_chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
