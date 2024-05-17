package core.entities.instances.multitileparts.tower;

import api.Position;
import api.entities.entitycapabilities.GuiDisplayable;
import api.entities.entitycapabilities.Redirector;
import api.events.gui.EnterDisplayableViewEvent;
import api.events.multitiles.towers.EnterTowerEvent;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import core.ImplDirection;
import core.ImplPosition;
import core.entities.Moving;
import core.entities.MultiTilePart;
import core.entities.instances.multitiles.Tower;
import core.utils.DeferredAsset;
import core.utils.display.BlockDisplay;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TowerEntrance extends MultiTilePart<Tower> implements Redirector, GuiDisplayable {
    private final DeferredAsset<ImageView> view = new DeferredAsset<>(
        () -> BlockDisplay.buildImageView("/assets/multitile_parts/tower/tower_entrance.png")
    );
    private final DeferredAsset<Entity> entity = new DeferredAsset<>(
        () -> {
            Entity entity = FXGL.entityBuilder()
                .view(view.get())
                .onClick(
                    (Consumer<Entity>) e -> getPosition()
                        .getWorld()
                        .getEventManager()
                        .callEvent(new EnterDisplayableViewEvent(getMultiTile()))
                )
                .buildAndAttach();
            entity.setVisible(false);
            return entity;
        }
    );

    public TowerEntrance(Position position, Tower multiTile) {
        super(position, multiTile);
    }

    @Override
    public void redirect(Moving m) {
        if (m.getDirection().getY() < 0) {
            List<Position> exitDirections = new ArrayList<>();
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    Position pos = new ImplPosition(getPosition().getWorld(), x, 0, z);
                    if (getPosition().clone().add(pos).getOccupant().isEmpty()) exitDirections.add(pos);
                }
            }
            if (exitDirections.isEmpty()) return;
            Position exitDir = exitDirections.get((int) (Math.random() * exitDirections.size()));
            m.setPosition(getPosition().clone().add(exitDir));
            m.setDirection(exitDir);
        }
        else {
            if (getPosition().clone().add(0, 1, 0).getOccupant().isPresent()) m.getDirection().multiply(-1);
            else {
                m.setPosition(getPosition().clone().add(0, 1, 0));
                m.setDirection(new ImplDirection(0, 1, 0));
                getPosition().getWorld().getEventManager().callEvent(new EnterTowerEvent(getMultiTile(), m));
            }
        }
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
