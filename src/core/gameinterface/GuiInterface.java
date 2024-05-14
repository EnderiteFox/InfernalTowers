package core.gameinterface;

import api.EventManager;
import api.Position;
import api.entities.entitycapabilities.GuiDisplayable;
import api.events.gui.EntityLoadEvent;
import api.events.gui.GuiDisplayGameEvent;
import api.events.occupants.OccupantSpawnEvent;
import api.gameinterface.InputInterface;
import api.world.World;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.input.KeyCode;

public class GuiInterface implements InputInterface {
    private final World world;

    public static final int TILE_SIZE = 50;
    private final double ZOOM_SPEED = 0.75;
    private final double CAM_SPEED = 0.5;

    private final String PRELOAD_LISTENER = "GuiInterfacePreloadListener";
    private boolean loaded = false;

    private double zoom = 1.0;
    private double camX = 0;
    private double camZ = 0;

    public GuiInterface(World world) {
        this.world = world;
        world.getEventManager().registerListener(OccupantSpawnEvent.class, PRELOAD_LISTENER, this::onOccupantSpawn);
        world.getEventManager().registerListener(EntityLoadEvent.class, this::onEntitySpawn);
    }

    public static double[] getScreenSpacePos(Position gridPos, double zoom, double camX, double camZ) {
        return new double[] {
            (gridPos.getX() - camX) * TILE_SIZE * zoom + FXGL.getAppWidth() / 2.0,
            (gridPos.getZ() - camZ) * TILE_SIZE * zoom + FXGL.getAppHeight() / 2.0
        };
    }

    void onOccupantSpawn(OccupantSpawnEvent event) {
        world.getEventManager().callDeferredEvent(EventManager.GUI_LOAD_REGISTRY, new EntityLoadEvent(event));
    }

    void onEntitySpawn(EntityLoadEvent spawnEvent) {
        OccupantSpawnEvent event = spawnEvent.getSpawnEvent();
        if (!(event.getOccupant() instanceof GuiDisplayable o)) return;
        o.getEntity();
        world.getEventManager().registerListener(
            GuiDisplayGameEvent.class,
            e -> o.updateNode(e.zoom(), e.camX(), e.camZ())
        );
    }

    @Override
    public void displayGame() {
        if (!loaded) {
            world.getEventManager().unregisterListener(OccupantSpawnEvent.class, PRELOAD_LISTENER);
            world.getEventManager().registerListener(OccupantSpawnEvent.class, o -> onEntitySpawn(new EntityLoadEvent(o)));
            loaded = true;
        }
        zoom = Math.max(0.1, Math.min(10, zoom));
        world.getEventManager().callEvent(new GuiDisplayGameEvent(zoom, camX, camZ));
    }

    @Override
    public boolean processInput() {
        return true;
    }

    @Override
    public void initInput() {
        FXGL.onKey(KeyCode.D, () -> camX += CAM_SPEED * (1 / zoom));
        FXGL.onKey(KeyCode.Z, () -> camZ -= CAM_SPEED * (1 / zoom));
        FXGL.onKey(KeyCode.Q, () -> camX -= CAM_SPEED * (1 / zoom));
        FXGL.onKey(KeyCode.S, () -> camZ += CAM_SPEED * (1 / zoom));
        FXGL.getPrimaryStage().getScene().setOnScroll(
            event -> {
                if (event.getDeltaY() > 0) zoom /= ZOOM_SPEED;
                if (event.getDeltaY() < 0) zoom *= ZOOM_SPEED;
            }
        );
        FXGL.onKey(
            KeyCode.NUMPAD0,
            () -> {
                camX = 0;
                camZ = 0;
                zoom = 1;
            }
        );
    }
}
