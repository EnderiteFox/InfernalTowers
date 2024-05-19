package api.events.occupants;

import api.EventManager;
import api.events.OccupantEvent;
import core.entities.Occupant;

/**
 * An event called when a {@link core.entities.instances.occupants.RotatingPanel} rotates
 */
public class PanelRotateEvent extends OccupantEvent {
    public PanelRotateEvent(Occupant occupant) {
        super(occupant);
    }

    @Override
    public String toString() {
        return EventManager.SILENT_EVENT;
    }
}
