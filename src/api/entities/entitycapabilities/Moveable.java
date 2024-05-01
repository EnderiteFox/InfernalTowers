package api.entities.entitycapabilities;

import api.Position;

/**
 * An interface representing an entity that is able to move
 */
public interface Moveable {
    /**
     * @return The current position of the moveable
     */
    Position getPresentPosition();

    /**
     * @return The position of the moveable at the next game tick
     */
    Position getTargetPosition();

    /**
     * Moves the moveable to the given position
     * @param pos The position to move this moveable to
     * @return {@code true} if the moveable was able to move, {@code false} otherwise
     */
    boolean moveTo(Position pos);
}
