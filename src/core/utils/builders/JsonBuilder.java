package core.utils.builders;

import core.utils.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class JsonBuilder<T> {
    protected final Map<String, Function<JsonParser, T>> builderMap = new HashMap<>();
    private final boolean debugMode;

    public JsonBuilder(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public JsonBuilder() {
        this(false);
    }

    public Optional<T> build(Map<String, Object> data) {
        if (!data.containsKey("type")) throw new IllegalArgumentException("Missing JSON key: type");
        Object obj = data.get("type");
        if (!(obj instanceof String type)) return Optional.empty();
        if (!builderMap.containsKey(type)) return Optional.empty();
        try {
            return Optional.of(builderMap.get(type).apply(new JsonParser(data)));
        } catch (Exception e) {
            if (debugMode) System.out.println("Warning: Error while loading type " + type + ":");
            throw e;
        }
    }
}
