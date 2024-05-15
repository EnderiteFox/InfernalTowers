package core.world;

import api.EventManager;
import api.Position;
import api.entities.Ticking;
import api.world.World;
import core.entities.MultiTile;
import core.entities.Occupant;

import java.util.*;

public class ImplWorld implements World {
    private final EventManager eventManager;
    private final Map<Position, Occupant> world = new TreeMap<>();
    private final List<MultiTile> multiTiles = new ArrayList<>();

    public ImplWorld(EventManager eventManager) {
        this.eventManager = eventManager;
    }

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
        multiTile.getOccupants().forEach(o -> o.getPosition().getWorld().addOccupant(o));
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
    public Optional<Occupant> getOccupant(UUID uuid) {
        if (uuid == null) return Optional.empty();
        for (Occupant o : world.values()) if (o.getUniqueId().equals(uuid)) return Optional.of(o);
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
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public void tick() {
        getAllOfType(Ticking.class).forEach(Ticking::tick);
    }
}
