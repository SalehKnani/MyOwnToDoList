package com.example.todo.core;

import com.google.gson.annotations.SerializedName;

public enum TaskStatus {
    @SerializedName("todo")
    TODO,
    @SerializedName(value = "in-progress", alternate = {"inprogress"})
    IN_PROGRESS,
    @SerializedName("done")
    DONE;

    // Keep for CLI input
    public static TaskStatus fromUser(String s) {
        return switch (s.toLowerCase()) {
            case "todo" -> TODO;
            case "in-progress", "inprogress" -> IN_PROGRESS;
            case "done" -> DONE;
            default -> throw new IllegalArgumentException("Unknown status: " + s);
        };
    }

    // Optional: CLI-friendly display
    public String toUser() {
        return switch (this) {
            case TODO -> "todo";
            case IN_PROGRESS -> "in-progress";
            case DONE -> "done";
        };
    }
}
