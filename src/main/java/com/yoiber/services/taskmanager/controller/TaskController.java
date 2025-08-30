package com.yoiber.services.taskmanager.controller;

import com.yoiber.services.taskmanager.dto.CreateTaskDTO;
import com.yoiber.services.taskmanager.dto.PaginatedResponseDTO;
import com.yoiber.services.taskmanager.dto.TaskResponseDTO;
import com.yoiber.services.taskmanager.dto.TaskSummaryDTO;
import com.yoiber.services.taskmanager.dto.UpdateTaskDTO;
import com.yoiber.services.taskmanager.entity.Task;
import com.yoiber.services.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "API REST para la gestión de tareas")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las tareas", description = "Recupera todas las tareas con paginación opcional y filtrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tareas recuperadas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getAllTasks(
            @Parameter(description = "Número de página (basado en 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filtrar por estado de completitud") @RequestParam(required = false) Boolean completed,
            @Parameter(description = "Palabra clave para búsqueda") @RequestParam(required = false) String search) {

        if (search != null && !search.trim().isEmpty()) {
            // Búsqueda por palabra clave - retorna lista simple
            List<Task> tasks = taskService.searchTasks(search.trim());
            List<TaskResponseDTO> taskDTOs = tasks.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(taskDTOs);

        } else if (completed != null) {
            // Filtrado por estado de completitud con paginación
            Pageable pageable = PageRequest.of(page, size);
            Page<Task> taskPage = taskService.getTasksByStatus(completed, pageable);

            PaginatedResponseDTO<TaskResponseDTO> response = createPaginatedResponse(taskPage);
            return ResponseEntity.ok(response);

        } else {
            // Todas las tareas con paginación
            Pageable pageable = PageRequest.of(page, size);
            Page<Task> taskPage = taskService.getAllTasks(pageable);

            PaginatedResponseDTO<TaskResponseDTO> response = createPaginatedResponse(taskPage);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tarea por ID", description = "Recupera una tarea específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @Parameter(description = "ID de la tarea") @PathVariable Long id) {

        Task task = taskService.getTaskById(id);
        TaskResponseDTO responseDTO = convertToResponseDTO(task);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    @Operation(summary = "Crear nueva tarea", description = "Crea una nueva tarea con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarea creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        Task createdTask = taskService.createTask(createTaskDTO.getTitle(), createTaskDTO.getDescription());
        TaskResponseDTO responseDTO = convertToResponseDTO(createdTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tarea", description = "Actualiza una tarea existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<TaskResponseDTO> updateTask(
            @Parameter(description = "ID de la tarea") @PathVariable Long id,
            @Valid @RequestBody UpdateTaskDTO updateTaskDTO) {

        Task updatedTask = taskService.updateTask(
                id,
                updateTaskDTO.getTitle(),
                updateTaskDTO.getDescription(),
                updateTaskDTO.getCompleted()
        );
        TaskResponseDTO responseDTO = convertToResponseDTO(updatedTask);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tarea", description = "Elimina una tarea por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarea eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID de la tarea") @PathVariable Long id) {

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Alternar estado de tarea", description = "Cambia el estado de completitud de una tarea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado cambiado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<TaskResponseDTO> toggleTaskCompletion(
            @Parameter(description = "ID de la tarea") @PathVariable Long id) {

        Task toggledTask = taskService.toggleTaskCompletion(id);
        TaskResponseDTO responseDTO = convertToResponseDTO(toggledTask);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/summary")
    @Operation(summary = "Obtener resumen de tareas", description = "Recupera un resumen simple de todas las tareas")
    @ApiResponse(responseCode = "200", description = "Resumen recuperado exitosamente")
    public ResponseEntity<List<TaskSummaryDTO>> getTasksSummary() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskSummaryDTO> summaryDTOs = tasks.stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaryDTOs);
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas", description = "Recupera estadísticas sobre las tareas")
    @ApiResponse(responseCode = "200", description = "Estadísticas recuperadas exitosamente")
    public ResponseEntity<TaskService.TaskStats> getTaskStats() {
        TaskService.TaskStats stats = taskService.getTskStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar tareas", description = "Busca tareas por palabra clave en título o descripción")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    public ResponseEntity<List<TaskResponseDTO>> searchTasks(
            @Parameter(description = "Palabra clave para buscar") @RequestParam String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Task> tasks = taskService.searchTasks(keyword.trim());
        List<TaskResponseDTO> taskDTOs = tasks.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskDTOs);
    }

    // Métodos auxiliares para conversión de DTOs
    private TaskResponseDTO convertToResponseDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    private TaskSummaryDTO convertToSummaryDTO(Task task) {
        return new TaskSummaryDTO(
                task.getId(),
                task.getTitle(),
                task.getCompleted(),
                task.getCreatedAt()
        );
    }

    private PaginatedResponseDTO<TaskResponseDTO> createPaginatedResponse(Page<Task> taskPage) {
        List<TaskResponseDTO> taskDTOs = taskPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(
                taskDTOs,
                taskPage.getTotalElements(),
                taskPage.getTotalPages(),
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.isFirst(),
                taskPage.isLast()
        );
    }
}