package core.entities.instances.occupants;

import api.Direction;
import api.Position;
import api.entities.Ticking;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.GuiDisplayable;
import api.entities.entitycapabilities.Redirector;
import api.events.occupants.OccupantSpawnEvent;
import com.almasb.fxgl.entity.Entity;
import core.ImplDirection;
import core.entities.Moving;
import core.entities.Occupant;
import core.utils.DeferredAsset;
import core.utils.display.BlockDisplay;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Grave extends Occupant implements Redirector, Ticking, ConsoleDisplayable, GuiDisplayable {
    private final DeferredAsset<ImageView> view = new DeferredAsset<>(
        () -> BlockDisplay.buildImageView("/assets/occupants/grave.png")
    );
    private final DeferredAsset<Entity> entity = new DeferredAsset<>(
        () -> BlockDisplay.buildEntity(view.get())
    );

    private final int cooldown;
    private final int health;

    private int currentCooldown = 0;

    public Grave(Position position, int cooldown, int health) {
        super(position);
        this.cooldown = cooldown;
        this.health = health;
    }

    @Override
    public void redirect(Moving m) {
        m.getDirection().multiply(-1);
    }

    @Override
    public void tick() {
        currentCooldown--;
        if (currentCooldown <= 0) {
            currentCooldown = cooldown;
            List<Direction> directions = new ArrayList<>();
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    Direction dir = new ImplDirection(x, 0, z);
                    if (getPosition().add(dir).getOccupant().isEmpty()) directions.add(dir);
                }
            }
            if (directions.isEmpty()) return;
            Direction spawnDir = directions.get((int) (Math.random() * directions.size()));
            Zombie zombie = new Zombie(getPosition().add(spawnDir), spawnDir, health);
            getPosition().getWorld().addOccupant(zombie);
            getPosition().getWorld().getEventManager().callEvent(new OccupantSpawnEvent(zombie));
        }
    }

    @Override
    public char toChar() {
        return 'G';
    }

    @Override
    public Entity getEntity() {
        return entity.get();
    }

    @Override
    public ImageView getView() {
        return view.get();
    }

    @Override
    public void updateNode(CameraState cameraState) {
        BlockDisplay.updateImageBlock(view.get(), entity.get(), getPosition(), cameraState);
    }
}
