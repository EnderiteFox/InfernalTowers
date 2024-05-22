package core.entities.instances.occupants;

import api.Direction;
import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.GuiDisplayable;
import api.entities.entitycapabilities.Redirector;
import com.almasb.fxgl.entity.Entity;
import core.entities.LivingEntity;
import core.entities.Moving;
import core.entities.Occupant;
import core.utils.DeferredAsset;
import core.utils.display.BlockDisplay;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

import java.util.Optional;
import java.util.UUID;

public class Zombie extends LivingEntity implements Redirector, ConsoleDisplayable, GuiDisplayable {
    private final DeferredAsset<ImageView> view = new DeferredAsset<>(
        () -> BlockDisplay.buildImageView("/assets/occupants/zombie.png")
    );
    private final DeferredAsset<Entity> entity = new DeferredAsset<>(() -> BlockDisplay.buildEntity(view.get()));

    public Zombie(Position position, Direction direction, double max_health) {
        super(position, direction, max_health);
    }

    public Zombie(Position position, Direction direction, UUID uuid, double max_health) {
        super(position, direction, uuid, max_health);
    }

    @Override
    public boolean moveTo(Position pos) {
        Optional<Occupant> occupant = getTargetPosition().getOccupant();
        occupant.ifPresent(c -> {if (c instanceof Moving) this.damage(1.0);});
        return super.moveTo(pos);
    }

    @Override
    public void redirect(Moving m) {
        m.getDirection().multiply(-1);
        this.damage(1.0);
    }

    @Override
    public char toChar() {
        return 'Z';
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
        updateHurtAnimation();
    }
}
