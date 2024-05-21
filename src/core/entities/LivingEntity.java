package core.entities;

import api.Direction;
import api.Position;
import api.events.occupants.EntityDeathEvent;
import api.events.occupants.OccupantRemoveEvent;

import java.util.UUID;

public abstract class LivingEntity extends Moving {
    private final double max_health;
    private double health;

    public LivingEntity(Position position, Direction direction, double max_health) {
        this(position, direction, UUID.randomUUID(), max_health);
    }

    public LivingEntity(Position position, Direction direction, UUID uuid, double max_health) {
        super(position, direction, uuid);
        this.max_health = max_health;
        this.health = max_health;
    }

    public void damage(double amount) {
        health -= amount;
        if (health <= 0) this.kill();
    }

    public void kill() {
        getPosition().getWorld().getEventManager().callEvent(new EntityDeathEvent(this));
        getPosition().getWorld().getEventManager().callEvent(new OccupantRemoveEvent(this));
        getPosition().getWorld().removeOccupant(this);
    }
}
