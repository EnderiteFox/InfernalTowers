package core.entities;

import api.Position;

import java.util.UUID;

/**
 * A class representing an entity occupying a tile on the map
 */
public abstract class Occupant {
    private Position position;
    private final UUID uuid;

    public Occupant(Position position) {
        this.position = position;
        this.uuid = UUID.randomUUID();
    }

    /**
     * <p>Returns a clone of the position of this entity
     * <p>This returns a clone for performance reasons, as the position of this entity should never be updated directly
     * <p>That's because the different entities use the Positions from the map to get the position of other entities, and
     * not updating the keys of the map accordingly could cause desyncs between the position instances
     * in the entities and in the world
     * @return A clone of the position of this entity
     */
    public Position getPosition() {
        return position.clone();
    }

    /**
     * A setter for the position of this entity
     * @param position The new position
     */
    public void setPosition(Position position) {
        this.position.getWorld().removeOccupant(this);
        this.position = position;
        position.getWorld().setOccupant(position, this);
    }

    /**
     * @return The UUID of this entity
     */
    public UUID getUniqueId() {
        return uuid;
    }
}
