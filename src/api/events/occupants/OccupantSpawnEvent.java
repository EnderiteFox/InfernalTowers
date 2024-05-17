package api.events.occupants;

import api.events.OccupantEvent;
import core.entities.Occupant;

public class OccupantSpawnEvent extends OccupantEvent {
    public OccupantSpawnEvent(Occupant occupant) {
        super(occupant);
    }

    @Override
    public String toString() {
        return getOccupant().getUniqueId() + " of type "
            + getOccupant().getClass().getSimpleName() + " spawned at " + getOccupant().getPosition();
    }
}
