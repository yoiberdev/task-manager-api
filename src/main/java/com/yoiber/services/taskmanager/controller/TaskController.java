package com.yoiber.services.taskmanager.controller;

import com.yoiber.services.taskmanager.dto.TaskResponseDTO;
import com.yoiber.services.taskmanager.entity.Task;
import com.yoiber.services.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "REST API for managing tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

//    @GetMapping
//    @Operation(summary = "Get all tasks", description = "Retrieve all tasks optional pagination and filtering")
//    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(
//            @Parameter(description = "Page number (0-based)") @RequestParam(value = "0") int page,
//            @Parameter(description = "Page size") @RequestParam(value = "10") int size,
//            @Parameter(description = "Filter by completion status") @RequestParam(required = false) Boolean completed,
//            ) {
////        if (completed != null) {
////            Pageable pageable = PageRequest.of(page, size);
////            Page<Task> taskPage = taskService.getTasksByStatus(completed, pageable);
////            List<TaskResponseDTO> tasks = taskPage.getContent().stream()
////                    .map(this::convertToResponseDTO)
////                    .collect(Collectors.toList());
////            return ResponseEntity;
////        }
//    }

//    public ResponseEntity<TaskResponseDTO> getTaskId {
//
//    }

}
