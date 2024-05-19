package api.events.occupants;

import api.Position;
import api.events.Event;
import core.entities.Moving;

/**
 * An event called when a {@link Moving} moves
 */
public class MoveEvent extends Event {
    private final Moving moving;
    private final Position from;
    private final Position to;

    public MoveEvent(Moving moving, Position from, Position to) {
        this.moving = moving;
        this.from = from;
        this.to = to;
    }

    public Moving getMoving() {
        return moving;
    }

    public Position from() {
        return from;
    }

    public Position to() {
        return to;
    }

    @Override
    public String toString() {
        return getMoving().getUniqueId() + " moved from " + from + " to " + to;
    }
}
