package core.gameinterface;

import api.Direction;
import api.EventManager;
import api.entities.GuiGlobalDisplayable;
import api.entities.entitycapabilities.GuiDisplayable;
import api.events.gui.EnterDisplayableViewEvent;
import api.events.gui.EntityLoadEvent;
import api.events.gui.GuiDisplayGameEvent;
import api.events.occupants.OccupantRemoveEvent;
import api.events.occupants.OccupantSpawnEvent;
import api.gameinterface.InputInterface;
import api.world.World;
import com.almasb.fxgl.dsl.FXGL;
import core.utils.display.CameraState;
import javafx.scene.input.KeyCode;

import java.util.Stack;

/**
 * The implementation of a {@link api.gameinterface.GameInterface} that uses an FXGL graphical interface to
 * display the game
 */
public class GuiInterface implements InputInterface {
    private final World world;
    private final Stack<DisplayState> displayStack = new Stack<>();

    public static final int TILE_SIZE = 50;
    private final double ZOOM_SPEED = 0.75;
    private final double CAM_SPEED = 0.5;

    private final String PRELOAD_LISTENER = "GuiInterfacePreloadListener";
    private boolean loaded = false;

    public GuiInterface(World world) {
        this.world = world;
        displayStack.push(new DisplayState(world, world.getDefaultCameraState()));
        world.getEventManager().registerListener(OccupantSpawnEvent.class, PRELOAD_LISTENER, this::onOccupantSpawn);
        world.getEventManager().registerListener(EntityLoadEvent.class, this::onEntitySpawn);
        world.getEventManager().registerListener(
            EnterDisplayableViewEvent.class,
            d -> {
                displayStack.peek().displayable.onLeaveView();
                displayStack.peek().displayable.setInView(false);
                displayStack.push(new DisplayState(d.getDisplayable(), d.getDisplayable().getDefaultCameraState()));
                displayStack.peek().displayable.onEnterView();
                displayStack.peek().displayable.setInView(true);
            }
        );
        world.getEventManager().registerListener(
            OccupantRemoveEvent.class,
            o -> {if (o instanceof GuiDisplayable displayable) displayable.getEntity().removeFromWorld();}
        );
    }

    /**
     * Calculates the screen space coordinates of a given position
     * @param gridPos The position to calculate screen space coordinates for
     * @param zoom The camera zoom multiplier
     * @param camX The X coordinate of the camera
     * @param camZ The Z coordinate of the camera
     * @return An array containing the calculated X and Z screen space coordinates
     */
    public static double[] getScreenSpacePos(Direction gridPos, double zoom, double camX, double camZ) {
        return new double[] {
            (gridPos.getX() - camX) * TILE_SIZE * zoom + FXGL.getAppWidth() / 2.0,
            (gridPos.getZ() - camZ) * TILE_SIZE * zoom + FXGL.getAppHeight() / 2.0
        };
    }

    /**
     * A listener for {@link OccupantSpawnEvent}s
     * @param event The event
     */
    void onOccupantSpawn(OccupantSpawnEvent event) {
        world.getEventManager().callDeferredEvent(EventManager.GUI_LOAD_REGISTRY, new EntityLoadEvent(event));
    }

    /**
     * A listener for {@link EntityLoadEvent}s
     * @param spawnEvent The event
     */
    void onEntitySpawn(EntityLoadEvent spawnEvent) {
        OccupantSpawnEvent event = spawnEvent.getSpawnEvent();
        if (!(event.getOccupant() instanceof GuiDisplayable o)) return;
        o.initDisplayable();
        o.getEntity();
    }

    @Override
    public void displayGame() {
        if (!loaded) {
            world.onEnterView();
            world.setInView(true);
            world.getEventManager().unregisterListener(OccupantSpawnEvent.class, PRELOAD_LISTENER);
            world.getEventManager().registerListener(OccupantSpawnEvent.class, o -> onEntitySpawn(new EntityLoadEvent(o)));
            loaded = true;
        }
        CameraState camera = displayStack.peek().cameraState;
        camera.setZoom(Math.max(0.1, Math.min(10, camera.zoom())));
        displayStack.peek().displayable.updateFrame(camera);
        world.getEventManager().callEvent(new GuiDisplayGameEvent(camera));
    }

    @Override
    public boolean processInput() {
        return true;
    }

    /**
     * Moves the currently used camera with the given amount
     * @param x The amount to move the camera in the x-axis
     * @param y The amount to move the camera in the y-axis
     * @param z The amount to move the camera in the z-axis
     */
    private void moveCamera(double x, int y, double z) {
        CameraState camera = displayStack.peek().cameraState;
        camera.setCamX(camera.camX() + x);
        camera.setCamY(camera.camY() + y);
        camera.setCamZ(camera.camZ() + z);
    }

    /**
     * @return the current zoom value
     */
    private double getCurrentZoom() {
        return displayStack.peek().cameraState.zoom();
    }

    @Override
    public void initInput() {
        FXGL.onKey(KeyCode.D, () -> moveCamera(CAM_SPEED * (1 / getCurrentZoom()), 0, 0));
        FXGL.onKey(KeyCode.Z, () -> moveCamera(0, 0, -CAM_SPEED * (1 / getCurrentZoom())));
        FXGL.onKey(KeyCode.Q, () -> moveCamera(-CAM_SPEED * (1 / getCurrentZoom()), 0, 0));
        FXGL.onKey(KeyCode.S, () -> moveCamera(0, 0, CAM_SPEED * (1 / getCurrentZoom())));
        FXGL.onKeyDown(KeyCode.UP, () -> moveCamera(0, 1, 0));
        FXGL.onKeyDown(KeyCode.DOWN, () -> moveCamera(0, -1, 0));
        FXGL.onKeyDown(
            KeyCode.ESCAPE,
            () -> {
                if (displayStack.size() <= 1) FXGL.getPrimaryStage().close();
                else {
                    displayStack.peek().displayable.onLeaveView();
                    displayStack.peek().displayable.setInView(false);
                    displayStack.pop();
                    displayStack.peek().displayable.onEnterView();
                    displayStack.peek().displayable.setInView(true);
                }
            }
        );
        FXGL.getPrimaryStage().getScene().setOnScroll(
            event -> {
                if (event.getDeltaY() > 0) displayStack.peek().cameraState.setZoom(getCurrentZoom() / ZOOM_SPEED);
                if (event.getDeltaY() < 0) displayStack.peek().cameraState.setZoom(getCurrentZoom() * ZOOM_SPEED);
            }
        );
        FXGL.onKey(
            KeyCode.NUMPAD0,
            () -> displayStack.peek().cameraState = displayStack.peek().displayable.getDefaultCameraState()
        );
    }

    /**
     * A class that stores a display state, composed of a {@link GuiGlobalDisplayable} and a {@link CameraState}
     */
    private static class DisplayState {
        public final GuiGlobalDisplayable displayable;
        public CameraState cameraState;

        public DisplayState(GuiGlobalDisplayable displayable, CameraState cameraState) {
            this.displayable = displayable;
            this.cameraState = cameraState;
        }
    }
}
