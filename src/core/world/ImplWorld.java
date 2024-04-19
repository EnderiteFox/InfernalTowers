package core.world;

import api.Position;
import api.entities.Ticking;
import api.world.World;
import core.entities.Occupant;

import java.util.*;

public class ImplWorld implements World {
    private final Map<Position, Occupant> world = new TreeMap<>();

    @Override
    public List<Occupant> getOccupants() {
        return new ArrayList<>(world.values());
    }

    @Override
    public Optional<Occupant> getOccupant(Position pos) {
        if (world.containsKey(pos)) return Optional.of(world.get(pos));
        return Optional.empty();
    }

    @Override
    public void setOccupant(Position pos, Occupant occupant) {
        world.put(pos, occupant);
    }

    @Override
    public void addOccupant(Occupant occupant) {
        setOccupant(occupant.getPosition(), occupant);
    }

    @Override
    public void removeOccupant(Position pos) {
        world.remove(pos);
    }

    @Override
    public void removeOccupant(Occupant occupant) {
        removeOccupant(occupant.getPosition());
    }
}
