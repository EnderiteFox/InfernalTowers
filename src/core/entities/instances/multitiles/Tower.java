package core.entities.instances.multitiles;

import api.Direction;
import api.Position;
import api.entities.GuiGlobalDisplayable;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.Building;
import api.events.multitiles.towers.CaptureTowerEvent;
import api.utils.CharGrid;
import api.world.World;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import core.ImplDirection;
import core.entities.MultiTile;
import core.entities.Occupant;
import core.entities.instances.multitileparts.tower.TowerEntrance;
import core.entities.instances.multitileparts.tower.TowerTop;
import core.gameinterface.GuiInterface;
import core.utils.DeferredAsset;
import core.utils.ImplCharGrid;
import core.utils.display.BlockDisplay;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tower extends MultiTile implements Building, GuiGlobalDisplayable {
    private final DeferredAsset<List<TowerDecoration>> decorations = new DeferredAsset<>(this::buildTowerDecoration);
    private final DeferredAsset<TowerDecoration> towerTop = new DeferredAsset<>(
        () -> {
            ImageView view = BlockDisplay.buildImageView(
                "/assets/multitile_parts/tower/view/tower_top.png",
                3, 3
            );
            return new TowerDecoration(
                BlockDisplay.buildEntity(view),
                view, new ImplDirection(0, 0, 0),
                3, 3
            );
        }
    );
    private final DeferredAsset<TowerDecoration> towerTopFlagged = new DeferredAsset<>(
        () -> {
            ImageView view = BlockDisplay.buildImageView(
                "/assets/multitile_parts/tower/view/tower_top_flagged.png",
                3, 3
            );
            return new TowerDecoration(
                BlockDisplay.buildEntity(view),
                view, new ImplDirection(0, 0, 0),
                3, 3
            );
        }
    );
    private boolean isInView = false;

    private UUID owner = null;
    private final int size;

    public Tower(Position pos) {
        this(pos, (int) (Math.random() * 9) + 1);
    }

    public Tower(Position pos, int size) {
        assert size >= 1;
        this.size = size;
        occupants.add(new TowerEntrance(pos, this));
        occupants.add(new TowerTop(pos.clone().add(0, 1 + size, 0), this));
    }

    public TowerTop getTop() {
        for (Occupant o : getOccupants()) if (o instanceof TowerTop t) return t;
        return null;
    }

    public TowerEntrance getEntrance() {
        for (Occupant o : getOccupants()) if (o instanceof TowerEntrance t) return t;
        return null;
    }

    @Override
    public List<Occupant> getOccupantsInside() {
        List<Occupant> occupants = new ArrayList<>();
        Position pos = getEntrance().getPosition().clone().add(0, 1, 0);
        while (
            (pos.getOccupant().isEmpty() || !getOccupants().contains(pos.getOccupant().get()))
                && pos.getZ() - getEntrance().getPosition().getZ() + 1 < size
        ) {
            pos.getOccupant().ifPresent(occupants::add);
            pos.add(0, 1, 0);
        }
        return occupants;
    }

    @Override
    public boolean isOccupied() {
        return !getOccupantsInside().isEmpty();
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        if (this.owner == null || !this.owner.equals(owner)) {
            World world = getTop().getPosition().getWorld();
            world.getOccupant(owner).ifPresent(
                    newO -> world.getEventManager().callEvent(
                        new CaptureTowerEvent(this, world.getOccupant(this.owner).orElse(null), newO)
                    )
            );
        }
        this.owner = owner;
    }

    @Override
    public CharGrid getInsideView() {
        CharGrid grid = new ImplCharGrid();
        grid.setChar(0, 0, '|');
        grid.setChar(1, 0, '#');
        grid.setChar(2, 0, '|');
        for (int i = 0; i < size; ++i) {
            grid.setChar(0, -1 - i, '|');
            grid.setChar(2, -1 - i, '|');
        }
        List<Occupant> inside = getOccupantsInside();
        inside.forEach(
            o -> {
                if (!(o instanceof ConsoleDisplayable displayable)) return;
                grid.setChar(
                    1,
                    getEntrance().getPosition().getY() - o.getPosition().getY(),
                    displayable.toChar()
                );
            }
        );
        grid.insertString(0, -size - 1, "/ \\");
        grid.setChar(1, -size - 2, '^');
        return grid;
    }

    private List<TowerDecoration> buildTowerDecoration() {
        List<TowerDecoration> entities = new ArrayList<>();
        ImageView view = BlockDisplay.buildImageView(
            "/assets/multitile_parts/tower/view/tower_bottom.png",
            3, 1
        );
        entities.add(
            new TowerDecoration(
                BlockDisplay.buildEntity(view),
                view,
                new ImplDirection(0, 0, 3 + size),
                3, 1
            )
        );
        for (int z = 3; z < size + 3; ++z) {
            for (int x = 0; x <= 2; x++) {
                view = BlockDisplay.buildImageView(
                    "/assets/multitile_parts/tower/view/tower_" + (x == 1 ? "background" : "wall") + ".png");
                entities.add(
                    new TowerDecoration(
                        BlockDisplay.buildEntity(view),
                        view,
                        new ImplDirection(x, 0, z),
                        1, 1
                    )
                );
            }
        }
        return entities;
    }

    @Override
    public CameraState getDefaultCameraState() {
        return new CameraState(
            (FXGL.getAppHeight() / (double) GuiInterface.TILE_SIZE) / (size + 5.0),
            1.5,
            0,
            (4 + size) / 2.0,
            true
        );
    }

    @Override
    public void updateFrame(CameraState cameraState) {
        decorations.get().forEach(
            d -> {
                BlockDisplay.updateImageBlock(
                    d.view, d.entity,
                    d.position, cameraState,
                    d.blockWidth, d.blockHeight
                );
                d.entity.setZIndex(-1);
            }
        );
        TowerDecoration mainTop = getOwner() == null ? towerTop.get() : towerTopFlagged.get();
        TowerDecoration hiddenTop = getOwner() == null ? towerTopFlagged.get() : towerTop.get();
        mainTop.entity.setVisible(true);
        BlockDisplay.updateImageBlock(
            mainTop.view, mainTop.entity,
            mainTop.position, cameraState,
            mainTop.blockWidth, mainTop.blockHeight
        );
        hiddenTop.entity.setVisible(false);
    }

    @Override
    public void onEnterView() {
        decorations.get().forEach(d -> d.entity.setVisible(true));
    }

    @Override
    public void onLeaveView() {
        decorations.get().forEach(d -> d.entity.setVisible(false));
        towerTop.get().entity.setVisible(false);
        towerTopFlagged.get().entity.setVisible(false);
    }

    @Override
    public boolean isInView() {
        return isInView;
    }

    @Override
    public void setInView(boolean inView) {
        isInView = inView;
    }

    private record TowerDecoration(Entity entity, ImageView view, Direction position, int blockWidth, int blockHeight) {}
}
