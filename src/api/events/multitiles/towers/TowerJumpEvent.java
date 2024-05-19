package api.events.multitiles.towers;

import api.events.OccupantEvent;
import core.entities.Occupant;
import core.entities.instances.multitiles.Tower;

/**
 * An event called when someone jumps from a {@link Tower} to another one of its {@link Tower}s
 */
public class TowerJumpEvent extends OccupantEvent {
    private final Tower fromTower;
    private final Tower toTower;

    public TowerJumpEvent(Occupant occupant, Tower fromTower, Tower toTower) {
        super(occupant);
        this.fromTower = fromTower;
        this.toTower = toTower;
    }

    public Tower fromTower() {
        return fromTower;
    }

    public Tower toTower() {
        return toTower;
    }

    @Override
    public String toString() {
        return getOccupant().getUniqueId() + " jumped from tower at "
            + fromTower.getTop().getPosition().clone().add(0, -1, 0)
            + " to " + toTower.getTop().getPosition().clone().add(0, -1, 0);
    }
}
