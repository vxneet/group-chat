package com.gpt.group_chat.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User sender;

    @ManyToOne
    @JoinColumn(name="group_id", nullable=false)
    private Group group;

    private LocalDateTime timestamp =  LocalDateTime.now();
}
