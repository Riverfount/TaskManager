package com.riverfount.taskmanager.controller;


import com.riverfount.taskmanager.exception.TaskNotFoundException;
import com.riverfount.taskmanager.model.Task;
import com.riverfount.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void getTaskByIdReturnsSpecificTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("title");
        task.setDescription("description");
        task.setDueDate(LocalDate.parse("2026-06-12"));
        when(taskService.findById(1L)).thenReturn(task);
        mockMvc.perform(get("/v1/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.dueDate").value("2026-06-12"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getTaskByIdReturnsNotFound() throws Exception {
        when(taskService.findById(99L)).thenThrow(new TaskNotFoundException(99L));
        mockMvc.perform(get("/v1/api/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Task not found: 99"));
    }

    @Test
    void createTaskRaiseErrorWithTitleEmpty() throws Exception {
        String json = """
                {
                    "title": "",
                    "description": "description"
                }
                """;

        mockMvc.perform(post("/v1/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    void createTaskReturnsTaskCreated() throws Exception {
        Task taskSalva = new Task();
        taskSalva.setId(1L);
        taskSalva.setTitle("title");
        taskSalva.setDescription("description");
        taskSalva.setDueDate(LocalDate.parse("2026-06-28"));

        when(taskService.create(any(Task.class))).thenReturn(taskSalva);

        String json = """
                {
                    "title": "title",
                    "description": "description",
                    "dueDate": "2026-06-28"
                }
        """;

        mockMvc.perform(post("/v1/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.dueDate").value("2026-06-28"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createTaskReturnsTaskCreatedWithCorrectID() throws Exception {
        Task taskSalva = new Task();
        taskSalva.setId(1L);
        taskSalva.setTitle("title");
        taskSalva.setDescription("description");
        taskSalva.setDueDate(LocalDate.parse("2026-06-28"));

        when(taskService.create(any(Task.class))).thenReturn(taskSalva);

        String json = """
                {
                    "id": 999,
                    "title": "title",
                    "description": "description",
                    "dueDate": "2026-06-28"
                }
        """;

        mockMvc.perform(post("/v1/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("title"));
    }
}
