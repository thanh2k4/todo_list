package com.example.todo_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.todo_backend.model.Task;
import com.example.todo_backend.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskRepository taskRepository;

    @Test
    public void testGetAllTasks() {
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new Task(1L, "Task 1", false));
        tasks.add(new Task(2L, "Task 2", true));
        tasks.add(new Task(3L, "Task 3", false));
        when(taskRepository.findAll()).thenReturn(tasks);
        List<Task> result = taskService.getAllTasks();
        assertEquals(tasks, result);
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task(1L, "Task 1", false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Task result = taskService.getTaskById(1L);
        assertEquals(task, result);
    }

    @Test
    public void testCreateTask() {
        Task task = new Task(1L, "Task 1", false);
        when(taskRepository.save(task)).thenReturn(task);
        Task result = taskService.createTask(task);
        assertEquals(task, result);
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task(1L, "Task 1", false);
        Task updatedTask = new Task(1L, "Task 1", true);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(updatedTask);
        Task result = taskService.updateTask(1L, updatedTask);
        assertEquals(updatedTask, result);
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task(1L, "Task 1", false);
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).delete(task);
    }
}
