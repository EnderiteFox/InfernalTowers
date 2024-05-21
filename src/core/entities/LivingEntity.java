package core.entities;

import api.Direction;
import api.Position;
import api.entities.entitycapabilities.GuiDisplayable;
import api.events.gui.GuiDisplayGameEvent;
import api.events.occupants.EntityDeathEvent;
import api.events.occupants.OccupantRemoveEvent;
import javafx.scene.effect.ColorAdjust;

import java.util.UUID;

public abstract class LivingEntity extends Moving {
    private final double max_health;
    private double health;
    private long lastHurtTime = -1;

    public LivingEntity(Position position, Direction direction, double max_health) {
        this(position, direction, UUID.randomUUID(), max_health);
    }

    public LivingEntity(Position position, Direction direction, UUID uuid, double max_health) {
        super(position, direction, uuid);
        this.max_health = max_health;
        this.health = max_health;
        if (this instanceof GuiDisplayable) {
            position.getWorld().getEventManager().registerListener(
                GuiDisplayGameEvent.class,
                e -> updateHurtAnimation()
            );
        }
    }

    public void damage(double amount) {
        health -= amount;
        lastHurtTime = System.currentTimeMillis();
        if (health <= 0) this.kill();
    }

    public void kill() {
        getPosition().getWorld().getEventManager().callEvent(new EntityDeathEvent(this));
        getPosition().getWorld().getEventManager().callEvent(new OccupantRemoveEvent(this));
        getPosition().getWorld().removeOccupant(this);
    }

    public void updateHurtAnimation() {
        if (!(this instanceof GuiDisplayable)) return;
        GuiDisplayable displayable = (GuiDisplayable) this;
        final long hurtAnimLength = 750;
        double timeSinceHurt = System.currentTimeMillis() - lastHurtTime;
        double progress;
        if (timeSinceHurt < 0 || timeSinceHurt > hurtAnimLength) progress = 0;
        else progress = 1 - (timeSinceHurt / hurtAnimLength);
        displayable.getView()
            .setEffect(new ColorAdjust((-0.6 * progress * progress * progress), 0, 0, 0));
    }
}
