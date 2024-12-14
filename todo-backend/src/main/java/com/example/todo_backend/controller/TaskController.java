package com.example.todo_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.todo_backend.model.Task;
import com.example.todo_backend.service.TaskService;
import com.example.todo_backend.utils.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {

        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getTasksByUser(
            @CookieValue(name = "token", required = false) String token, HttpServletResponse response) {
        if (token == null) {
            String jwt = jwtUtil.generateToken();
            Cookie cookie = new Cookie("token", jwt);
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setAttribute("SameSite", "None");
            response.addCookie(cookie);
            return ResponseEntity.ok().build();
        }
        List<Task> tasks = taskService.getTasksByUser(token);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task,
            @CookieValue(name = "token", required = false) String token) {
        Task createdTask = taskService.createTask(task, token);
        return ResponseEntity.status(201).body(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id,
            @CookieValue(name = "token", required = false) String token) {
        Task task = taskService.getTaskById(id, token);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
            @RequestBody Task task,
            @CookieValue(name = "token", required = false) String token) {
        Task updatedTask = taskService.updateTask(id, task, token);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id,
            @CookieValue(name = "token", required = false) String token) {
        taskService.deleteTask(id, token);
        return ResponseEntity.noContent().build();
    }

}
