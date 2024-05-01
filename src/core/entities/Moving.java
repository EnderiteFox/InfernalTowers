package core.entities;

import api.Direction;
import api.Position;
import api.entities.Moveable;
import api.entities.Redirector;
import api.entities.Ticking;

/**
 * An abstract class used for entities that are able to move
 */
public abstract class Moving extends Occupant implements Moveable, Ticking {
    private Direction direction;

    public Moving(Position position, Direction direction) {
        super(position);
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
        return (Position) getPresentPosition().clone().add(direction);
    }

    @Override
    public boolean moveTo(Position pos) {
        if (pos.getOccupant().isEmpty()) setPosition(pos);
        else {
            if (pos.getOccupant().get() instanceof Redirector redirector) redirector.redirect(this);
            else return false;
        }
        return true;
    }

    @Override
    public void tick() {
        moveTo(getTargetPosition());
    }
}
