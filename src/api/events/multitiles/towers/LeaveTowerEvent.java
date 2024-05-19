package api.events.multitiles.towers;

import core.entities.Occupant;
import core.entities.instances.multitiles.Tower;

/**
 * An event called when an entity leaves a {@link Tower}
 */
public class LeaveTowerEvent extends EnterTowerEvent {
    public LeaveTowerEvent(Tower tower, Occupant occupant) {
        super(tower, occupant);
    }

    @Override
    public String toString() {
        return occupant().getUniqueId() + " left tower at " + tower().getEntrance().getPosition();
    }
}
