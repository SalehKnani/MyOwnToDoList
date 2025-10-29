package com.example.todo.cli;

import com.example.todo.core.*;

import java.util.*;

public class Cli {
    private final TaskService service;

    public Cli(TaskService service) { this.service = service; }

    public int run(String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) { printHelp(); return 0; }
        try {
            return switch (args[0]) {
                case "add" -> { need(args.length >= 2, "Usage: add \"description\"");
                    var t = service.add(join(args,1));
                    System.out.println("Task added successfully (ID: " + t.getId() + ")");
                    yield 0; }
                case "update" -> { long id = id(args,1); need(args.length>=3,"Usage: update <id> \"new description\"");
                    service.update(id, join(args,2));
                    System.out.println("Task updated successfully"); yield 0; }
                case "delete" -> { service.delete(id(args,1)); System.out.println("Task deleted successfully"); yield 0; }
                case "mark-in-progress" -> { service.mark(id(args,1), TaskStatus.IN_PROGRESS); System.out.println("Task marked in-progress"); yield 0; }
                case "mark-done" -> { service.mark(id(args,1), TaskStatus.DONE); System.out.println("Task marked done"); yield 0; }
                case "list" -> {
                    Optional<TaskStatus> f = Optional.empty();
                    if (args.length >= 2) f = Optional.of(TaskStatus.fromUser(args[1]));
                    var tasks = service.list(f);
                    tasks.forEach(Cli::printTask);
                    yield 0;
                }
                default -> { System.err.println("Unknown command: " + args[0]); printHelp(); yield 2; }
            };
        } catch (NumberFormatException e) {
            System.err.println("ID must be a number.");
            return 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
    }

    private static String join(String[] a, int from) { return String.join(" ", Arrays.copyOfRange(a, from, a.length)); }
    private static void need(boolean c, String m) { if(!c) throw new IllegalArgumentException(m); }
    private static long id(String[] a, int i) { need(a.length>i, "Missing <id>"); return Long.parseLong(a[i]); }

    private static void printTask(Task t) {
        System.out.printf("#%d [%s] %s (created %s, updated %s)%n",
                t.getId(),
                t.getStatus().toUser(),
                t.getDescription(),
                t.getCreatedAt(),
                t.getUpdatedAt());
    }

    private static void printHelp() {
        System.out.println("""
        Usage:
          add "desc"
          update <id> "new desc"
          delete <id>
          mark-in-progress <id>
          mark-done <id>
          list [todo|in-progress|done]
          help
        """);
    }
}
