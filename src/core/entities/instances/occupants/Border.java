package core.entities.instances.occupants;

import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.GuiDisplayable;
import api.entities.entitycapabilities.Redirector;
import com.almasb.fxgl.entity.Entity;
import core.entities.Moving;
import core.entities.Occupant;
import core.utils.DeferredAsset;
import core.utils.display.BlockDisplay;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

/**
 * A Border entity, making other entities bounce back
 */
public class Border extends Occupant implements Redirector, ConsoleDisplayable, GuiDisplayable {
    private final DeferredAsset<ImageView> view = new DeferredAsset<>(
        () -> BlockDisplay.buildImageView("/assets/occupants/brick.png")
    );
    private final DeferredAsset<Entity> entity = new DeferredAsset<>(() -> BlockDisplay.buildEntity(view.get()));

    public Border(Position position) {
        super(position);
    }

    @Override
    public void redirect(Moving m) {
        Position pos = m.getPosition();
        if (m.getDirection().getY() != 0) m.getDirection().setY(0);
        if (m.getDirection().getX() == 0 || m.getDirection().getZ() == 0) m.getDirection().multiply(-1);
        else {
             Position xPos = pos.clone().add(m.getDirection().getX(), 0, 0);
             Position zPos = pos.clone().add(0, 0, m.getDirection().getZ());
             if (xPos.getOccupant().isEmpty() && zPos.getOccupant().isPresent()) {
                 pos = xPos;
                 m.getDirection().multiply(1, 1, -1);
             }
             else if (xPos.getOccupant().isPresent() && zPos.getOccupant().isEmpty()) {
                 pos = zPos;
                 m.getDirection().multiply(-1, 1, 1);
             }
             else m.getDirection().multiply(-1);
        }
        m.setPosition(pos);
    }

    @Override
    public char toChar() {
        return '#';
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
