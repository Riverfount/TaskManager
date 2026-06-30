package com.riverfount.taskmanager.service;

import com.riverfount.taskmanager.exception.TaskNotFoundException;
import com.riverfount.taskmanager.model.Task;
import com.riverfount.taskmanager.model.TaskStatus;
import com.riverfount.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    public void setUp() {
        this.task = new Task();
        this.task.setId(1L);
        this.task.setDescription("description");
        this.task.setTitle("title");
        this.task.setDueDate(LocalDate.parse("2026-06-12"));
    }

    @Test
    void findAllShouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        List<Task> response = taskService.findAll();
        verify(taskRepository).findAll();
        assertThat(response).hasSize(1);
        assertThat(response).containsExactly(task);
    }

    @Test
    void findByIdShouldReturnTaskWhenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Task response = taskService.findById(1L);
        assertThat(response).isEqualTo(task);
    }

    @Test
    void findByIdShouldThrowWhenNotExists() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.findById(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createShouldSaveAndReturnTask() {
        when(taskRepository.save(task)).thenReturn(task);
        Task response = taskService.create(task);
        verify(taskRepository).save(response);
        assertThat(response).isEqualTo(task);
    }

    @Test
    void deleteShouldDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        taskService.delete(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteShouldThrowWhenNotExists() {
        when(taskRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> taskService.delete(99L))
                .isInstanceOf(TaskNotFoundException.class);
        verify(taskRepository, never()).deleteById(any());

    }

    @Test
    void updateShouldUpdateTask() {
        Task updatedTask = new Task();
        updatedTask.setTitle("updated title");
        updatedTask.setDescription("updated description");
        updatedTask.setDueDate(LocalDate.parse("2026-06-13"));
        updatedTask.setStatus(TaskStatus.DONE);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Task response = taskService.update(1L, updatedTask);

        verify(taskRepository).findById(1L);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("updated title");
        assertThat(response.getDescription()).isEqualTo("updated description");
        assertThat(response.getDueDate()).isEqualTo(LocalDate.parse("2026-06-13"));
        assertThat(response.getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void updateShouldThrowWhenNotExists() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.update(99L, task))
                .isInstanceOf(TaskNotFoundException.class);
    }
}