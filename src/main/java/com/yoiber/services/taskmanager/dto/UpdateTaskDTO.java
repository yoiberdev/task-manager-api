package com.yoiber.services.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public class UpdateTaskDTO {

    @Schema(description = "Update task title", example = "Complete project documentation - Updated")
    @Size(max = 255, message = "Title must not exceed 255 charecters")
    private String title;

    @Schema(description = "Updated task description", example = "Write comprehensive documentation for the API with examples")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Schema(description = "Task completion status", example = "true")
    private Boolean completed;

    public UpdateTaskDTO() {
    }

    public UpdateTaskDTO(String title, String description, Boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
