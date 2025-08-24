package com.gpt.group_chat.repository;

import com.gpt.group_chat.model.Vlog;
import com.gpt.group_chat.model.VlogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VlogRepository extends JpaRepository<Vlog, Long> {
    List<Vlog> findByGroupIdOrderByCreatedAtDesc(Long groupId);
    Optional<Vlog> findByGroupIdAndAssignmentDate(Long groupId, LocalDate date);
    List<Vlog> findByAssignedUserIdOrderByCreatedAtDesc(Long userId);
    List<Vlog> findByGroupIdAndStatus(Long groupId, VlogStatus status);

    @Query("SELECT v FROM Vlog v WHERE v.group.id = :groupId AND v.assignmentDate = :date")
    Optional<Vlog> findTodaysVlogAssignment(@Param("groupId") Long groupId, @Param("date") LocalDate date);
}
