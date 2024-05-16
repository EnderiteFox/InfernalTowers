package api.events.multitiles.towers;

import api.events.TowerEvent;
import core.entities.Occupant;
import core.entities.instances.multitiles.Tower;

public class EnterTowerEvent extends TowerEvent {
    private final Occupant occupant;

    public EnterTowerEvent(Tower tower, Occupant occupant) {
        super(tower);
        this.occupant = occupant;
    }

    public Occupant occupant() {
        return occupant;
    }

    @Override
    public String toString() {
        return "Entered tower at " + tower().getEntrance().getPosition();
    }
}
