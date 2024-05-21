package api.events.occupants;

import api.events.OccupantEvent;
import core.entities.Occupant;

public class OccupantRemoveEvent extends OccupantEvent {
    public OccupantRemoveEvent(Occupant occupant) {
        super(occupant);
    }

    @Override
    public String toString() {
        return getOccupant().getUniqueId() + " removed at " + getOccupant().getPosition();
    }
}
