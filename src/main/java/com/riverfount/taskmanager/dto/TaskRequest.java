package com.riverfount.taskmanager.dto;

import com.riverfount.taskmanager.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema
public record TaskRequest(
        @NotBlank(message = "This field is required") @Size(max = 200, message = "Size must be between 0 and 200") String title,
        @Size(max = 2000, message = "Max size 2000") String description,
        TaskStatus status,
        @FutureOrPresent(message = "The due date cannot be in the past.") LocalDate dueDate
) {
}
