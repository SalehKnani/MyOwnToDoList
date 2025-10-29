package com.example.todo.store;

import com.example.todo.core.Task;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Instant;

final class InstantAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
    @Override public JsonElement serialize(Instant src, Type t, JsonSerializationContext ctx) {
        return new JsonPrimitive(src.toString()); // ISO-8601 same as before
    }
    @Override public Instant deserialize(JsonElement json, Type t, JsonDeserializationContext ctx)
            throws JsonParseException {
        return Instant.parse(json.getAsString());
    }
}

final class Gsons {
    static final Gson PRETTY = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .setPrettyPrinting()        // nice, stable formatting (like your current output)
            // .serializeNulls()        // keep commented: omit nulls to match your current schema
            .create();

    static final Type TASK_LIST = new TypeToken<java.util.List<Task>>(){}.getType();
}
