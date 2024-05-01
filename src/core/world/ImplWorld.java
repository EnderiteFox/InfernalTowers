package core.world;

import api.Position;
import api.entities.Ticking;
import api.world.World;
import core.entities.MultiTile;
import core.entities.Occupant;

import java.util.*;

public class ImplWorld implements World {
    private final Map<Position, Occupant> world = new TreeMap<>();
    private final List<MultiTile> multiTiles = new ArrayList<>();

    @Override
    public List<Occupant> getOccupants() {
        return new ArrayList<>(world.values());
    }

    @Override
    public List<MultiTile> getMultiTiles() {
        return multiTiles;
    }

    @Override
    public <T> List<T> getAllOfType(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        getOccupants().forEach(
            o -> {
                if (clazz.isInstance(o)) list.add(clazz.cast(o));
            }
        );
        getMultiTiles().forEach(
            m -> {
                if (clazz.isInstance(m)) list.add(clazz.cast(m));
            }
        );
        return list;
    }

    @Override
    public void addMultiTile(MultiTile multiTile) {
        if (multiTiles.contains(multiTile)) return;
        multiTiles.add(multiTile);
        multiTile.getOccupants().forEach(this::addOccupant);
    }

    @Override
    public void removeMultiTile(MultiTile multiTile) {
        multiTiles.remove(multiTile);
        multiTile.getOccupants().forEach(this::removeOccupant);
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

    @Override
    public void tick() {
        for (Occupant occupant : getOccupants()) {
            if (occupant instanceof Ticking ticking) ticking.tick();
        }
        for (MultiTile multiTile : getMultiTiles()) {
            if (multiTile instanceof Ticking ticking) ticking.tick();
        }
    }
}
