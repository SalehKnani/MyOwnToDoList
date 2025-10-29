package com.example.todo.core;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    public interface Repo {
        List<Task> findAll();
        void saveAll(List<Task> tasks);
    }

    private final Repo repo;

    public TaskService(Repo repo) { this.repo = repo; }

    public Task add(String description) {
        var tasks = new ArrayList<>(repo.findAll());
        long nextId = tasks.stream().mapToLong(Task::getId).max().orElse(0) + 1;
        var now = Instant.now();
        var t = new Task(nextId, description, TaskStatus.TODO, now, now);
        tasks.add(t);
        repo.saveAll(tasks);
        return t;
    }

    public List<Task> list(Optional<TaskStatus> filter) {
        var all = repo.findAll();
        return filter.<List<Task>>map(f ->
                        all.stream().filter(t -> t.getStatus()==f).collect(Collectors.toList()))
                .orElse(all);
    }

    public Task update(long id, String newDesc) {
        var ts = new ArrayList<>(repo.findAll());
        var t = ts.stream().filter(x -> x.getId()==id).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Task " + id + " not found"));
        t.setDescription(newDesc);
        t.setUpdatedAt(Instant.now());
        repo.saveAll(ts);
        return t;
    }

    public Task mark(long id, TaskStatus status) {
        var ts = new ArrayList<>(repo.findAll());
        var t = ts.stream().filter(x -> x.getId()==id).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Task " + id + " not found"));
        t.setStatus(status);
        t.setUpdatedAt(Instant.now());
        repo.saveAll(ts);
        return t;
    }

    public void delete(long id) {
        var ts = new ArrayList<>(repo.findAll());
        if (!ts.removeIf(t -> t.getId()==id))
            throw new NoSuchElementException("Task " + id + " not found");
        repo.saveAll(ts);
    }
}
