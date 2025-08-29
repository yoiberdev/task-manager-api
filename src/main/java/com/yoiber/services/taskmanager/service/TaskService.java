package com.yoiber.services.taskmanager.service;

import com.yoiber.services.taskmanager.entity.Task;
import com.yoiber.services.taskmanager.exception.TaskNotFoundException;
import com.yoiber.services.taskmanager.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /*
     * Get all Tasks
     */
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAllByOrderByCreatedAtDesc();
    }

    /*
     * Get all tasks with pagination
     */
    @Transactional(readOnly = true)
    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    /*
     * Get task by ID
     */
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    /*
     * Create new task
     */
    public Task createTask(String title, String description) {
        Task task = new Task(title, description);
        return taskRepository.save(task);
    }

    /*
     * Update an existing task
     */
    public Task updateTask(Long id, String title, String description, Boolean completed) {
        Task existingTask = getTaskById(id);

        if (title != null && !title.trim().isEmpty()) {
            existingTask.setTitle(title);
        }

        if (description != null) {
            existingTask.setDescription(description);
        }

        if (completed != null) {
            existingTask.setCompleted(completed);
        }

        return taskRepository.save(existingTask);
    }

    /*
     * Delete a task
     */
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    /*
     * Toggle task completion status
     */
    public Task toggleTaskCompletion(Long id) {
        Task task = getTaskById(id);
        task.setCompleted(!task.getCompleted());
        return taskRepository.save(task);
    }

    /*
     * Get tasks by completion status
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(Boolean completed) {
        return taskRepository.findByCompleted(completed);
    }

    /*
     * Get tasks by completion status with pagination
     */
    @Transactional(readOnly = true)
    public Page<Task> getTasksByStatus(Boolean completed, Pageable pageable) {
        return taskRepository.findByCompleted(completed, pageable);
    }

    /*
     * Search tasks by keyword
     */
    @Transactional(readOnly = true)
    public List<Task> searchTasks(String keyword) {
        return taskRepository.findByTitleOrDescriptionContainingIgnoreCase(keyword);
    }

    /*
     * Get task stadistics
     */
    @Transactional(readOnly = true)
    public TaskStats getTskStats () {
        long totalTasks = taskRepository.count();
        long completedTasks = taskRepository.countByCompleted(true);
        long pendingTasks = taskRepository.countByCompleted(false);

        return new TaskStats(totalTasks, completedTasks, pendingTasks);
    }

    public static class TaskStats {
        private final long totalTasks;
        private final long completedTasks;
        private final long pendingTasks;

        public TaskStats(long totalTasks, long completedTasks, long pendingTasks) {
            this.totalTasks = totalTasks;
            this.completedTasks = completedTasks;
            this.pendingTasks = pendingTasks;
        }

        public long getTotalTasks() {
            return totalTasks;
        }

        public long getCompletedTasks() {
            return completedTasks;
        }

        public long getPendingTasks() {
            return pendingTasks;
        }

        public double getCompletionRate() {
            return totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0.0;
        }
    }

}
