package api.events.multitiles.quantumbox;

import api.events.OccupantEvent;
import core.entities.Occupant;
import core.entities.instances.occupants.QuantumBox;

/**
 * An event called when an entity enters a {@link QuantumBox}
 */
public class EnterBoxEvent extends OccupantEvent {
    private final QuantumBox box;
    public EnterBoxEvent(Occupant occupant, QuantumBox box) {
        super(occupant);
        this.box = box;
    }

    public QuantumBox getBox() {
        return box;
    }

    @Override
    public String toString() {
        return getOccupant().getUniqueId() + " entered box at " + box.getPosition();
    }
}
