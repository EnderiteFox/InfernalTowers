package core.utils.builders;

import api.Position;
import api.world.World;
import core.utils.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class JsonBuilder<T> {
    protected final Map<String, Function<JsonParser, T>> builderMap = new HashMap<>();
    protected final boolean debugMode;

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

    public Position requirePosition(JsonParser json, String jsonKey, World world)  {
        return json.<Map<String, Object>>getObjectAtPath(jsonKey).map(
            map -> json.parsePosition(map, world)
                .orElseThrow(
                    () -> new IllegalArgumentException(
                        "Failed to parse position from " + json.displayJson(map)
                    )
                )
        ).orElseThrow(
            () -> new IllegalArgumentException("Missing Json key: " + jsonKey)
        );
    }

    public <U> U requireKey(JsonParser json, String jsonKey) {
        return json.<U>getObjectAtPath(jsonKey)
            .orElseThrow(
                () -> new IllegalArgumentException(
                    "Missing Json key: " + jsonKey
                )
            );
    }
}
