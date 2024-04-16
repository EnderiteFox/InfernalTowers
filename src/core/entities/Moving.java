package core.entities;

import api.Position;
import api.world.World;

public abstract class Moving extends Occupant {
    private Position direction;

    public Moving(Position position, Position direction) {
        super(position);
        this.direction = direction;
    }

    public Position getDirection() {
        return direction;
    }

    public void setDirection(Position direction) {
        this.direction = direction;
    }
}
