package api.entities.builder;

import api.Position;
import core.entities.Occupant;

import java.util.Optional;

public interface EntityBuilder<T> {
    Optional<Occupant> build(T data, Position position);
}
