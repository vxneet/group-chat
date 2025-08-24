package com.gpt.group_chat.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="vlogs")
public class Vlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name="assigned_user_id", nullable = false)
    private User assignedUser;

    @Column(name="assigned_date")
    private LocalDate assignmentDate;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private VlogStatus status = VlogStatus.ASSIGNED;

    private String vlogUrl;
    private String description;

    @Column(name="submitted_at")
    private LocalDateTime submittedAt;
}
