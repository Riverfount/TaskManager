package com.riverfount.taskmanager.controller;


import com.riverfount.taskmanager.dto.TaskRequest;
import com.riverfount.taskmanager.dto.TaskResponse;
import com.riverfount.taskmanager.mapper.TaskMapper;
import com.riverfount.taskmanager.model.Task;
import com.riverfount.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all taks", description = "Returns a list of all tasks from Database.")
    @ApiResponse(responseCode = "200", description = "Found all tasks or a empty list, if there aren't tasks.")
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks() {
        List<Task> tasks = taskService.findAll();
        List<TaskResponse> response = tasks.stream().map(TaskMapper::toResponse).toList();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Get task by ID", description = "Returns a task detail by its ID,")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The details of a Task."),
            @ApiResponse(
                    responseCode = "404",
                    description = "Taks not found.",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        return ResponseEntity.ok().body(TaskMapper.toResponse(taskService.findById(id)));
    }

    @Operation(summary = "Crete task", description = "Create a new task.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task creted."),
            @ApiResponse(
                    responseCode = "400",
                    description = "Some error in payload.",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        Task createdTask = taskService.create(TaskMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskMapper.toResponse(createdTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok().body(TaskMapper.toResponse(taskService.update(id, TaskMapper.toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
