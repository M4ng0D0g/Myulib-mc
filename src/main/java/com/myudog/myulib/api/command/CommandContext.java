package com.myudog.myulib.api.command;

import java.util.Map;
import java.util.Objects;

public record CommandContext(String sourceId, String commandName, Map<String, String> arguments) {
    public CommandContext {
        sourceId = Objects.requireNonNullElse(sourceId, "");
        commandName = Objects.requireNonNullElse(commandName, "");
        arguments = arguments == null ? Map.of() : Map.copyOf(arguments);
    }
}

