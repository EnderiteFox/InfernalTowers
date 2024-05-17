package core.entities.instances.multitileparts.tower;

import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.GuiDisplayable;
import api.entities.entitycapabilities.Redirector;
import api.events.gui.EnterDisplayableViewEvent;
import api.events.multitiles.towers.TowerJumpEvent;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import core.entities.Moving;
import core.entities.MultiTilePart;
import core.entities.instances.multitiles.Tower;
import core.utils.DeferredAsset;
import core.utils.display.BlockDisplay;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.function.Consumer;

public class TowerTop extends MultiTilePart<Tower> implements Redirector, ConsoleDisplayable, GuiDisplayable {
    private final DeferredAsset<ImageView> view = new DeferredAsset<>(
        () -> BlockDisplay.buildImageView("/assets/multitile_parts/tower/tower_top.png")
    );
    private final DeferredAsset<Entity> entity = new DeferredAsset<>(
        () -> {
            Entity entity = FXGL.entityBuilder()
                .view(view.get())
                .onClick((Consumer<Entity>) e -> getPosition()
                    .getWorld()
                    .getEventManager()
                    .callEvent(new EnterDisplayableViewEvent(getMultiTile()))
                )
                .buildAndAttach();
            entity.setVisible(false);
            return entity;
        }
    );

    public TowerTop(Position position, Tower multiTile) {
        super(position, multiTile);
    }

    @Override
    public char toChar() {
        return 'T';
    }

    @Override
    public void redirect(Moving m) {
        getMultiTile().setOwner(m.getUniqueId());
        List<Tower> towers = getPosition().getWorld().getAllOfType(Tower.class);
        towers.removeIf(t -> t.getOwner() == null || !t.getOwner().equals(m.getUniqueId()));
        towers.removeIf(t -> t.getTop().getPosition().clone().add(0, -1, 0).getOccupant().isPresent());
        if (!towers.isEmpty() && Math.random() < 0.5) {
            Tower t = towers.get((int) (Math.random() * towers.size()));
            m.setPosition(t.getTop().getPosition().clone().add(0, -1, 0));
            getPosition().getWorld().getEventManager()
                .callEvent(new TowerJumpEvent(m, getMultiTile(), t));
        }
        m.getDirection().multiply(-1);
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
