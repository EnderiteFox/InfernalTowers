package core.entities;

import api.Position;

/**
 * A class representing an entity occupying a tile on the map
 */
public abstract class Occupant {
    private Position position;

    public Occupant(Position position) {
        this.position = position;
    }

    /**
     * @return The position of this entity
     */
    public Position getPosition() {
        return position;
    }

    /**
     * A setter for the position of this entity
     * @param position The new position
     */
    public void setPosition(Position position) {
        this.position = position;
    }
}
