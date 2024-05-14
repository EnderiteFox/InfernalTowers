package api.events;

import core.entities.instances.multitiles.Tower;

public class TowerEvent extends Event {
    private final Tower tower;

    public TowerEvent(Tower tower) {
        this.tower = tower;
    }

    public Tower tower() {
        return tower;
    }
}
