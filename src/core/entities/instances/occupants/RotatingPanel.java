package core.entities.instances.occupants;

import api.Direction;
import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.GuiDisplayable;
import api.entities.entitycapabilities.Redirector;
import api.events.occupants.PanelRotateEvent;
import com.almasb.fxgl.entity.Entity;
import core.entities.Moving;
import core.entities.Occupant;
import core.utils.DeferredAsset;
import core.utils.display.BlockDisplay;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

/**
 * A panel that makes {@link Moving}s bounce differently based on its orientation, and rotates afterward
 */
public class RotatingPanel extends Occupant implements Redirector, ConsoleDisplayable, GuiDisplayable {
    private final DeferredAsset<ImageView> view = new DeferredAsset<>(
        () -> BlockDisplay.buildImageView("/assets/occupants/rotating_panel.png")
    );
    private final DeferredAsset<Entity> entity = new DeferredAsset<>(() -> BlockDisplay.buildEntity(view.get()));

    private boolean isRotated;

    public RotatingPanel(Position position, boolean isRotated) {
        super(position);
        this.isRotated = isRotated;
    }

    @Override
    public void redirect(Moving m) {
        Direction dir = m.getDirection().clone();
        if (dir.getX() != 0 && dir.getZ() != 0) {
            dir.multiply(-1);
            isRotated = !isRotated;
            getPosition().getWorld().getEventManager().callEvent(new PanelRotateEvent(this));
            return;
        }
        if (dir.getX() != 0) {
            dir.setZ(dir.getX() * (isRotated ? -1 : 1));
            dir.setX(0);
        }
        else if (dir.getZ() != 0) {
            dir.setX(dir.getZ() * (isRotated ? -1 : 1));
            dir.setZ(0);
        }
        Position p = getPosition().add(dir);
        p.getOccupant().ifPresentOrElse(
            o -> m.getDirection().multiply(-1),
            () -> {
                m.setPosition(p);
                m.setDirection(dir);
            }
        );
        isRotated = !isRotated;
        getPosition().getWorld().getEventManager().callEvent(new PanelRotateEvent(this));
    }

    @Override
    public char toChar() {
        return isRotated ? '/' : '\\';
    }

    @Override
    public void initDisplayable() {
        getPosition().getWorld().getEventManager().registerListener(
            PanelRotateEvent.class, e -> {
                if (e.getOccupant() != this) return;
                view.get().setRotate(view.get().getRotate() + 90);
            }
        );
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
