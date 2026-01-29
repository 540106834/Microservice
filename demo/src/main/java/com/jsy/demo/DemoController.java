package com.jsy.demo;

import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/tasks")
public class DemoController {

    // 模拟数据库
    private final Map<Long, Task> taskStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // CREATE
    @PostMapping
    public Task create(@RequestBody Task task) {
        long id = idGenerator.getAndIncrement();
        task.id = id;
        taskStore.put(id, task);
        return task;
    }

    // READ - list
    @GetMapping
    public Collection<Task> list() {
        return taskStore.values();
    }
    
    @GetMapping("/health")
    public String health() {
        return "200 UP";
    }
    // READ - by id
    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        Task task = taskStore.get(id);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }
        return task;
    }

    // UPDATE
    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task updated) {
        Task existing = taskStore.get(id);
        if (existing == null) {
            throw new RuntimeException("Task not found");
        }
        existing.title = updated.title;
        existing.status = updated.status;
        return existing;
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskStore.remove(id);
    }
}

