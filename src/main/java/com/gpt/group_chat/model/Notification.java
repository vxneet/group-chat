package com.gpt.group_chat.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User recipient;

    @ManyToOne
    @JoinColumn(name="group_id", nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt=LocalDateTime.now();

    private boolean isRead = false;
    @ManyToOne
    @JoinColumn(name="vlog_id")
    private Vlog relatedVlog;
}
