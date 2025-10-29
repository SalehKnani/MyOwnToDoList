package com.example.todo;

import com.example.todo.cli.Cli;
import com.example.todo.core.TaskService;
import com.example.todo.store.TaskStore;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        // JSON in current directory; created if missing
        var store = new TaskStore(Path.of("tasks.json"));
        var service = new TaskService(store);
        var cli = new Cli(service);
        System.exit(cli.run(args));
    }
}
