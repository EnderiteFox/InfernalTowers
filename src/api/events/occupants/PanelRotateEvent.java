package api.events.occupants;

import api.EventManager;
import api.events.OccupantEvent;
import core.entities.Occupant;

public class PanelRotateEvent extends OccupantEvent {
    public PanelRotateEvent(Occupant occupant) {
        super(occupant);
    }

    @Override
    public String toString() {
        return EventManager.SILENT_EVENT;
    }
}
