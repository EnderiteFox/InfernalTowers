package core.entities.instances.occupants;

import api.Direction;
import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.GuiDisplayable;
import api.entities.entitycapabilities.Redirector;
import com.almasb.fxgl.entity.Entity;
import core.entities.Moving;
import core.utils.DeferredAsset;
import core.utils.display.BlockDisplay;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

import java.util.Optional;
import java.util.UUID;

/**
 * A human entity
 */
public class Human extends Moving implements Redirector, ConsoleDisplayable, GuiDisplayable {
    private final DeferredAsset<ImageView> view = new DeferredAsset<>(
        () -> BlockDisplay.buildImageView("/assets/occupants/human.png")
    );
    private final DeferredAsset<Entity> entity = new DeferredAsset<>(() -> BlockDisplay.buildEntity(view.get()));

    public Human(Position position) {
        super(
            position,
            Direction.getNonZeroRandom()
        );
    }

    public Human(Position position, UUID uuid) {
        super(position, Direction.getNonZeroRandom(), uuid);

    }

    @Override
    public void redirect(Moving m) {
        m.getDirection().multiply(-1);
    }

    @Override
    public char toChar() {
        return 'H';
    }

    @Override
    public Entity getEntity() {
        return entity.get();
    }

    @Override
    public void updateNode(CameraState cameraState) {
        BlockDisplay.updateImageBlock(view.get(), entity.get(), getPosition(), cameraState);
    }
}
