package core.entities;

import api.Position;
import api.world.World;

public abstract class Occupant {
    private Position position;

    public Occupant(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
