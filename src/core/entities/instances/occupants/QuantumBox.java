package core.entities.instances.occupants;

import api.Direction;
import api.EventManager;
import api.Position;
import api.entities.Building;
import api.entities.GuiGlobalDisplayable;
import api.entities.Ticking;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.GuiDisplayable;
import api.entities.entitycapabilities.Redirector;
import api.events.gui.EnterDisplayableViewEvent;
import api.events.multitiles.quantumbox.EnterBoxEvent;
import api.events.multitiles.quantumbox.LeaveBoxEvent;
import api.utils.CharGrid;
import api.world.World;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import core.ImplPosition;
import core.entities.Moving;
import core.entities.Occupant;
import core.gameinterface.ConsoleInterface;
import core.utils.DeferredAsset;
import core.utils.display.BlockDisplay;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A box that is bigger inside than outside, in a Doctor Who fashion
 */
public class QuantumBox
    extends Occupant
    implements Building, Ticking, ConsoleDisplayable, Redirector, GuiDisplayable, GuiGlobalDisplayable
{
    private final DeferredAsset<ImageView> view = new DeferredAsset<>(
        () -> BlockDisplay.buildImageView("/assets/occupants/quantum_box.png")
    );
    private final DeferredAsset<Entity> entity = new DeferredAsset<>(
        () -> {
            Entity entity = FXGL.entityBuilder()
                .view(view.get())
                .onClick(
                    (Consumer<Entity>) e -> getPosition()
                        .getWorld()
                        .getEventManager()
                        .callEvent(new EnterDisplayableViewEvent(this))
                )
                .buildAndAttach();
            entity.setVisible(false);
            return entity;
        }
    );
    private final DeferredAsset<List<BoxBorder>> borderEntities = new DeferredAsset<>(this::buildBorders);
    private boolean isInView = false;

    private final World world;
    private final int width;
    private final int height;
    private ConsoleInterface consoleInterface = null;

    public QuantumBox(Position position, World world, int width, int height) {
        super(position);
        this.world = world;
        this.width = width;
        this.height = height;
    }

    public QuantumBox(Position position, World world, int size) {
        this(position, world, size, size);
    }

    /**
     * Builds the fake border blocks used in the graphical interface view of the inside of the box
     * @return The list of {@link BoxBorder}s
     */
    private List<BoxBorder> buildBorders() {
        List<Position> borderPos = new ArrayList<>();
        for (int i = -1; i <= width; ++i) {
            borderPos.add(new ImplPosition(world, i, 0, -1));
            borderPos.add(new ImplPosition(world, i, 0, height));
        }
        for (int i = 0; i < height; ++i) {
            borderPos.add(new ImplPosition(world, -1, 0, i));
            borderPos.add(new ImplPosition(world, width, 0, i));
        }
        List<BoxBorder> borders = new ArrayList<>();
        for (Position pos : borderPos) {
            ImageView view = BlockDisplay.buildImageView("/assets/occupants/quantum_box_border.png");
            Entity entity = BlockDisplay.buildEntity(view);
            borders.add(new BoxBorder(entity, view, pos));
        }
        return borders;
    }

    /**
     * Makes a {@link Moving} entity enter the box
     * @param m The moving that enters the box
     */
    public void enterBox(Moving m) {
        Direction dir = m.getDirection().clone().normalize();
        Position enterPos = new ImplPosition(
            world,
            dir.getX() < 0 ? width - 1 : dir.getX() == 0 ? width / 2 : 0,
            0,
            dir.getZ() < 0 ? height - 1 : dir.getZ() == 0 ? height / 2 : 0
        );
        if (enterPos.getOccupant().isPresent()) m.getDirection().multiply(-1);
        else m.setPosition(enterPos);
        getPosition().getWorld().getEventManager().callEvent(new EnterBoxEvent(m, this));
    }

    /**
     * Makes a {@link Moving} entity leave the box
     * @param m The moving that leaves the box
     */
    public void exitBox(Moving m) {
        Position exitPos = getPosition().clone().add(m.getDirection().clone().normalize());
        if (exitPos.getOccupant().isPresent()) m.getDirection().multiply(-1);
        else {
            m.setPosition(exitPos);
            getPosition().getWorld().getEventManager().callEvent(new LeaveBoxEvent(m, this));
        }
    }

    @Override
    public boolean isOccupied() {
        if (world.getOccupants().stream().anyMatch(Moving.class::isInstance)) return true;
        List<Building> buildings = world.getAllOfType(Building.class);
        return buildings.stream().anyMatch(Building::isOccupied);
    }

    @Override
    public List<Occupant> getOccupantsInside() {
        return world.getOccupants();
    }

    @Override
    public CharGrid getInsideView() {
        if (consoleInterface == null) consoleInterface = new ConsoleInterface(world);
        CharGrid worldGrid = consoleInterface.buildDisplayGrid();
        worldGrid.setChar(-1, -1, '/');
        worldGrid.setChar(-1, height, '\\');
        worldGrid.setChar(width, -1, '\\');
        worldGrid.setChar(width, height, '/');
        for (int i = 0; i < width; ++i) {
            worldGrid.setChar(i, -1, '-');
            worldGrid.setChar(i, height, '-');
        }
        for (int i = 0; i < height; ++i) {
            worldGrid.setChar(-1, i, '|');
            worldGrid.setChar(width, i, '|');
        }
        CharGrid buildings = consoleInterface.buildBuildingsGrid();
        if (buildings != null) worldGrid.addSidePanel(buildings);
        return worldGrid;
    }

    @Override
    public void tick() {
        world.getAllOfType(Moving.class).forEach(m -> {if (isOut(m.getTargetPosition())) exitBox(m);});
        world.tick();
    }

    /**
     * Checks if a {@link Position} is outside the boundaries of the box or not
     * @param pos The position to check
     * @return {@code true} if the position is outside the box, {@code false} otherwise
     */
    private boolean isOut(Position pos) {
        return pos.getX() < 0 || pos.getX() >= width || pos.getZ() < 0 || pos.getZ() >= height;
    }

    @Override
    public char toChar() {
        return 'B';
    }

    @Override
    public void redirect(Moving m) {
        enterBox(m);
    }

    @Override
    public void initDisplayable() {
        EventManager eventManager = getPosition().getWorld().getEventManager();
        eventManager.registerListener(
            EnterBoxEvent.class,
            e -> {
                if (e.getBox() != this) return;
                if (!(e.getOccupant() instanceof GuiDisplayable)) return;
                GuiDisplayable displayable = (GuiDisplayable) e.getOccupant();
                if (isInView) displayable.getEntity().setVisible(true);
                if (getPosition().getWorld().isInView()) displayable.getEntity().setVisible(false);
            }
        );
        eventManager.registerListener(
            LeaveBoxEvent.class,
            e -> {
                if (e.getBox() != this) return;
                if (!(e.getOccupant() instanceof GuiDisplayable)) return;
                GuiDisplayable displayable = (GuiDisplayable) e.getOccupant();
                if (isInView) displayable.getEntity().setVisible(false);
                if (getPosition().getWorld().isInView()) displayable.getEntity().setVisible(true);
            }
        );
        world.initDisplayable();
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

    @Override
    public CameraState getDefaultCameraState() {
        return new CameraState(1.0, 0, 0, 0, true);
    }

    @Override
    public void updateFrame(CameraState cameraState) {
        world.updateFrame(cameraState);
        borderEntities.get().forEach(b -> BlockDisplay.updateImageBlock(b.view, b.entity, b.position, cameraState));
    }

    @Override
    public void onEnterView() {
        world.onEnterView();
        borderEntities.get().forEach(b -> b.entity.setVisible(true));
    }

    @Override
    public void onLeaveView() {
        world.onLeaveView();
        borderEntities.get().forEach(b -> b.entity.setVisible(false));
    }

    @Override
    public boolean isInView() {
        return isInView;
    }

    @Override
    public void setInView(boolean inView) {
        isInView = inView;
        world.setInView(inView);
    }

    /**
     * A record that stores the information of a box border, which acts like a ghost block
     */
    private static class BoxBorder {
        public final Entity entity;
        public final ImageView view;
        public final Position position;

        public BoxBorder(Entity entity, ImageView view, Position position) {
            this.entity = entity;
            this.view = view;
            this.position = position;
        }
    }
}
