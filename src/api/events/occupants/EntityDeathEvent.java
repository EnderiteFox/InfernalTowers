package api.events.occupants;

import api.events.Event;
import core.entities.LivingEntity;

public class EntityDeathEvent extends Event {
    private final LivingEntity entity;

    public EntityDeathEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "Entity of type " + entity.getClass().getSimpleName() + " is dead at " + entity.getPosition();
    }
}
