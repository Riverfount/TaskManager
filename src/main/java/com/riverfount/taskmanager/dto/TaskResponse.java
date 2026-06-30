package com.riverfount.taskmanager.dto;

import com.riverfount.taskmanager.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;


@Schema
public record TaskResponse(
        Long id,
        String title,
        String description,
        LocalDate dueDate,
        TaskStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
