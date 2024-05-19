package core.utils.builders;

import api.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * A class that can build objects from chars
 * @param <T> The type of object this builder can build
 */
public abstract class TxtBuilder<T> {
    protected final Map<Predicate<Character>, BiFunction<Character, Position, T>> builderMap = new HashMap<>();

    /**
     * <p>Registers a builder for any character in the given string.
     * <p>This is equivalent to registering a builder
     * with the predicate {@code chr -> chars.contains(String.valueOf(chr))}
     * @param chars The characters to register this builder for
     * @param builderFunc The builder function
     */
    protected void registerFromString(String chars, BiFunction<Character, Position, T> builderFunc) {
        builderMap.put(
            chr -> chars.contains(String.valueOf(chr)), builderFunc
        );
    }

    /**
     * Builds an object from a char and a position
     * @param data The char to build the object from
     * @param position The position of the char in the world
     * @return an optional of the built object if built successfully, or an empty optional if no builder was registered
     * for this char
     */
    public Optional<T> build(Character data, Position position) {
        for (Predicate<Character> predicate : builderMap.keySet()) {
            if (predicate.test(data)) return Optional.of(builderMap.get(predicate).apply(data, position));
        }
        return Optional.empty();
    }
}
