package com.riverfount.taskmanager.mapper;

import com.riverfount.taskmanager.dto.TaskRequest;
import com.riverfount.taskmanager.dto.TaskResponse;
import com.riverfount.taskmanager.model.Task;

public class TaskMapper {
    private TaskMapper() {
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public static Task toModel(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        task.setDueDate(taskRequest.dueDate());
        if (taskRequest.status() != null) {
            task.setStatus(taskRequest.status());
        }
        return task;
    }
}
