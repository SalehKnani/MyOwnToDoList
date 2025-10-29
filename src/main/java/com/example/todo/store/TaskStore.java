package com.example.todo.store;

import com.example.todo.core.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TaskStore implements TaskService.Repo {
    private static final String FILE_NAME = "tasks.json";
    private final Path jsonPath;
    private final Gson gson = Gsons.PRETTY; // from helper above

    public TaskStore(Path jsonPath) {
        this.jsonPath = jsonPath;
        ensureFile();
    }
    public TaskStore() { this(Paths.get(FILE_NAME)); }

    private void ensureFile() {
        try {
            if (Files.notExists(jsonPath)) {
                Files.createDirectories(jsonPath.getParent() == null ? Path.of(".") : jsonPath.getParent());
                Files.writeString(jsonPath, "[]", UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot create " + jsonPath, e);
        }
    }

    @Override
    public List<Task> findAll() {
        try {
            if (Files.size(jsonPath) == 0) return List.of();
            try (var r = Files.newBufferedReader(jsonPath, UTF_8)) {
                var list = gson.fromJson(r, Gsons.TASK_LIST);
                return list != null ? (List<Task>) list : List.of();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + jsonPath, e);
        }
    }

    @Override
    public void saveAll(List<Task> tasks) {
        Path tmp = jsonPath.resolveSibling(jsonPath.getFileName() + ".tmp");
        try (var w = Files.newBufferedWriter(tmp, UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            gson.toJson(tasks, w);  // pretty JSON, ISO instants, enum strings as specified
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write temp " + tmp, e);
        }
        try {
            Files.move(tmp, jsonPath,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);   // <-- keep atomic write
        } catch (IOException e) {
            // best-effort cleanup: ignore failures
            try { Files.deleteIfExists(tmp); } catch (IOException ignored) {}
            throw new UncheckedIOException("Cannot replace " + jsonPath, e);
        }
    }
}
