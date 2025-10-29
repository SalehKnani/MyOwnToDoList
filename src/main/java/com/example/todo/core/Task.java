package com.example.todo.core;

import java.time.Instant;

public class Task {
    private long id;
    private String description;
    private TaskStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public Task(long id, String description, TaskStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() { return id; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setDescription(String d){ this.description = d; }
    public void setStatus(TaskStatus s){ this.status = s; }
    public void setUpdatedAt(Instant t){ this.updatedAt = t; }
}
