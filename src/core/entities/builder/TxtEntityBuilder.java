package core.entities.builder;

import api.Position;
import api.entities.builder.EntityBuilder;
import core.entities.Occupant;
import core.entities.instances.Border;
import core.entities.instances.Human;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class TxtEntityBuilder implements EntityBuilder<Character> {
    private final Map<Predicate<Character>, BiFunction<Character, Position, Occupant>> builderMap = new HashMap<>();

    public TxtEntityBuilder() {
        registerFromString("H", (chr, pos) -> new Human(pos));
        registerFromString("#", (chr, pos) -> new Border(pos));
    }

    private void registerFromString(String chars, BiFunction<Character, Position, Occupant> builderFunc) {
        builderMap.put(
            chr -> chars.contains(String.valueOf(chr)), builderFunc
        );
    }

    @Override
    public Optional<Occupant> build(Character data, Position position) {
        for (Predicate<Character> predicate : builderMap.keySet()) {
            if (predicate.test(data)) return Optional.of(builderMap.get(predicate).apply(data, position));
        }
        return Optional.empty();
    }
}
