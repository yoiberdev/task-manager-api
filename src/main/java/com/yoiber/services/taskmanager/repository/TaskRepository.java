package com.yoiber.services.taskmanager.repository;

import com.yoiber.services.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompleted(Boolean completed);

    Page<Task> findByCompleted(Boolean completed, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE (t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Task> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT t FROM Task t WHERE (t.title) LIKE LOWER(CONCAT('%', :title, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> findByTitleOrDescriptionContainingIgnoreCase(@Param("keyword") String keyword);

    long countByCompleted(Boolean completed);

    @Query("SELECT t FROM Task t ORDER BY t.createdAt DESC")
    List<Task> findAllByOrderByCreatedAtDesc();

    Page<Task> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
