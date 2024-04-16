package core.world;

import api.Position;
import api.world.World;
import core.entities.Occupant;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImplWorld implements World {
    private final Map<Position, Occupant> world = new HashMap<>();

    @Override
    public Optional<Occupant> getOccupant(Position pos) {
        if (world.containsKey(pos)) return Optional.of(world.get(pos));
        return Optional.empty();
    }

    @Override
    public void setOccupant(Position pos, Occupant occupant) {
        world.put(pos, occupant);
    }
}
