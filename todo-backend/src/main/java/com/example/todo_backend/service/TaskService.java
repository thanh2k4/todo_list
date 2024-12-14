package com.example.todo_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.todo_backend.repository.TaskRepository;
import com.example.todo_backend.utils.JwtUtil;
import com.example.todo_backend.model.Task;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByUser(String token) {
        String userId = jwtUtil.extractUserId(token);
        return taskRepository.findByUserId(userId);
    }

    public Task getTaskById(Long id, String token) {
        String userId = jwtUtil.extractUserId(token);
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null && task.getUserId().equals(userId)) {
            return task;
        }
        return null;
    }

    public Task createTask(Task task, String token) {
        String userId = jwtUtil.extractUserId(token);
        task.setUserId(userId);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task, String token) {
        String userId = jwtUtil.extractUserId(token);
        Task existingTask = taskRepository.findById(id).orElse(null);

        if (existingTask == null || !existingTask.getUserId().equals(userId)) {
            return null;
        }

        existingTask.setTitle(task.getTitle());
        existingTask.setCompleted(task.getCompleted());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id, String token) {
        String userId = jwtUtil.extractUserId(token);
        Task task = taskRepository.findById(id).orElse(null);

        if (task != null && task.getUserId().equals(userId)) {
            taskRepository.deleteById(id);
        }
    }

}
