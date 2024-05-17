package api.events.multitiles.quantumbox;

import core.entities.Occupant;
import core.entities.instances.occupants.QuantumBox;

public class LeaveBoxEvent extends EnterBoxEvent {
    public LeaveBoxEvent(Occupant occupant, QuantumBox box) {
        super(occupant, box);
    }

    @Override
    public String toString() {
        return getOccupant().getUniqueId() + " left box at " + getBox().getPosition();
    }
}
