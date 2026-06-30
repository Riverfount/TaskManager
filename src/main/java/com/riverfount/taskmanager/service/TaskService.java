package com.riverfount.taskmanager.service;

import com.riverfount.taskmanager.exception.TaskNotFoundException;
import com.riverfount.taskmanager.model.Task;
import com.riverfount.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task create(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Task update(Long id, Task updatedTask) {
        Task taskToUpdate = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        taskToUpdate.setDueDate(updatedTask.getDueDate());
        taskToUpdate.setDescription(updatedTask.getDescription());
        taskToUpdate.setStatus(updatedTask.getStatus());
        taskToUpdate.setTitle(updatedTask.getTitle());
        return taskToUpdate;
    }

    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
