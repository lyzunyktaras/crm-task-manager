package com.sample.crm.repository;

import com.sample.crm.entity.Task;
import com.sample.crm.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByClientId(Long clientId);

    @Query("""
            SELECT t FROM Task t
            WHERE t.dueDate BETWEEN :startTime AND :endTime
            """)
    List<Task> findTasksDueInTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    Integer countByStatus(TaskStatus status);
}
