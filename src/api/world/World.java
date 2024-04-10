package api.world;

import api.Position;
import core.entities.Occupant;

import java.util.Map;
import java.util.Optional;

public interface World {
    Map<Position, Occupant> getTiles();
    Optional<Occupant> getOccupant(Position pos);
}
