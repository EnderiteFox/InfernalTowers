package core.utils.builders;

import api.Position;
import api.world.World;
import core.utils.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * A class used for building objects from json components
 * @param <T> The type of object that this builder can build
 */
public abstract class JsonBuilder<T> {
    protected final Map<String, Function<JsonParser, T>> builderMap = new HashMap<>();
    protected final boolean debugMode;

    public JsonBuilder(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public JsonBuilder() {
        this(false);
    }

    /**
     * Builds an object from the given data
     * @param data The data to use to build the object
     * @return an optional of the built object if the object was built successfully, or an empty optional if no builder
     * was registered for the given data
     */
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

    /**
     * Requires a position to be present in the json data. Throws an exception if the position is not present
     * @param json The json data to search the position in
     * @param jsonKey The key of the position
     * @param world The world to set as the world of the built position
     * @return the built position if it was found and built successfully
     */
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

    /**
     * Requires a type of data to be present at the given key. Throws an exception if the data is not found
     * @param json The json data to search
     * @param jsonKey The key of the wanted data
     * @return The json object required if present
     * @param <U> The type of data to search for
     */
    public <U> U requireKey(JsonParser json, String jsonKey) {
        return json.<U>getObjectAtPath(jsonKey)
            .orElseThrow(
                () -> new IllegalArgumentException(
                    "Missing Json key: " + jsonKey
                )
            );
    }
}
