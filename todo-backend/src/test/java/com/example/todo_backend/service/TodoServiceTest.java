// package com.example.todo_backend.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.mockito.junit.jupiter.MockitoExtension;
// import com.example.todo_backend.model.Task;
// import com.example.todo_backend.repository.TaskRepository;
// import com.example.todo_backend.utils.JwtUtil;

// @ExtendWith(MockitoExtension.class)
// public class TodoServiceTest {

// @Mock
// private TaskRepository taskRepository;

// @Mock
// private JwtUtil jwtUtil;

// @InjectMocks
// private TaskService taskService;

// private String validToken = "valid-token";
// private String invalidToken = "invalid-token";
// private String userId = "12345";

// @BeforeEach
// public void setup() {
// MockitoAnnotations.openMocks(this);
// }

// @Test
// public void testGetAllTasks_withValidToken() {
// List<Task> tasks = new ArrayList<>();
// tasks.add(new Task(1L, "Task 1", userId, false));
// tasks.add(new Task(2L, "Task 2", userId, true));
// tasks.add(new Task(3L, "Task 3", userId, false));

// when(jwtUtil.validateToken(validToken)).thenReturn(true);
// when(jwtUtil.extractUserId(validToken)).thenReturn(userId);

// when(taskRepository.findByUserId(userId)).thenReturn(tasks);

// List<Task> result = taskService.getTasksByUser(validToken);

// assertEquals(tasks, result);
// }

// @Test
// public void testGetAllTasks_withInvalidToken() {
// List<Task> tasks = new ArrayList<>();
// tasks.add(new Task(1L, "Task 1", userId, false));
// tasks.add(new Task(2L, "Task 2", userId, true));
// tasks.add(new Task(3L, "Task 3", userId, false));

// when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

// String newUserId = "new-user-id";
// when(taskRepository.findByUserId(newUserId)).thenReturn(tasks);

// List<Task> result = taskService.getTasksByUser(invalidToken);

// assertEquals(tasks, result);
// }

// @Test
// public void testGetTaskById() {
// Task task = new Task(1L, "Task 1", userId, false);
// when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
// Task result = taskService.getTaskById(1L, userId);
// assertEquals(task, result);
// }

// @Test
// public void testCreateTask() {
// Task task = new Task(1L, "Task 1", userId, false);
// when(taskRepository.save(task)).thenReturn(task);
// when(jwtUtil.validateToken(validToken)).thenReturn(true);
// when(jwtUtil.extractUserId(validToken)).thenReturn(userId);

// Task result = taskService.createTask(task, validToken);
// assertEquals(task, result);
// }

// @Test
// public void testUpdateTask() {
// Task task = new Task(1L, "Task 1", userId, false);
// Task updatedTask = new Task(1L, "Task 1", userId, true);

// when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
// when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

// Task result = taskService.updateTask(1L, updatedTask, userId);
// assertEquals(updatedTask, result);
// }

// @Test
// public void testDeleteTask() {
// doNothing().when(taskRepository).deleteById(1L);
// when(jwtUtil.validateToken(validToken)).thenReturn(true);
// when(jwtUtil.extractUserId(validToken)).thenReturn(userId);

// taskService.deleteTask(1L, validToken);
// verify(taskRepository, times(1)).deleteById(1L);
// }
// }
