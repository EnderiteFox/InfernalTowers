package api.events;

import core.entities.Occupant;

public abstract class OccupantEvent extends Event {
    private final Occupant occupant;

    public OccupantEvent(Occupant occupant) {
        this.occupant = occupant;
    }

    public Occupant getOccupant() {
        return occupant;
    }
}
