package core.entities;

import api.Direction;
import api.Position;
import api.entities.entitycapabilities.Moveable;
import api.entities.entitycapabilities.Redirector;
import api.entities.Ticking;
import api.events.occupants.MoveEvent;

import java.util.UUID;

/**
 * An abstract class used for entities that are able to move
 */
public abstract class Moving extends Occupant implements Moveable, Ticking {
    private Direction direction;

    public Moving(Position position, Direction direction) {
        super(position);
        this.direction = direction;
    }

    public Moving(Position position, Direction direction, UUID uuid) {
        super(position, uuid);
        this.direction = direction;
    }

    /**
     * @return The velocity of this entity
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * A setter for the velocity of this entity
     * @param direction The new direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Position getPresentPosition() {
        return getPosition();
    }

    @Override
    public Position getTargetPosition() {
        return getPresentPosition().clone().add(direction);
    }

    @Override
    public boolean moveTo(Position pos) {
        Position from = getPosition();
        if (pos.getOccupant().isEmpty()) {
            setPosition(pos);
            getPosition().getWorld().getEventManager().callEvent(new MoveEvent(this, from, getPosition()));
        }
        else {
            if (pos.getOccupant().get() instanceof Redirector) {
                ((Redirector) pos.getOccupant().get()).redirect(this);
                if (!getPosition().equals(from)) {
                    getPosition().getWorld().getEventManager().callEvent(new MoveEvent(this, from, getPosition()));
                }
            }
            else return false;
        }
        return true;
    }

    @Override
    public void tick() {
        moveTo(getTargetPosition());
    }
}
