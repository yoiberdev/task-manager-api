package com.yoiber.services.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data required to create a new task")
public class CreateTaskDTO {

    @Schema(description = "Task title", example = "Complete project documentation")
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Schema(description = "Task description", example = "Write comprehensive documentation for API")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    public CreateTaskDTO() {
    }

    public CreateTaskDTO(String title, String description) {
        this.title = title;
        this.description = description;
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
}
