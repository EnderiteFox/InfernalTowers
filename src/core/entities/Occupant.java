package core.entities;

import api.Position;

public abstract class Occupant {
    private Position position;

    public Occupant(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
