package api.events.gui;

import api.EventManager;
import api.events.Event;

public class GuiDisplayGameEvent extends Event {
    private final double zoom;
    private final double camX;
    private final double camZ;

    public GuiDisplayGameEvent(double zoom, double camX, double camZ) {
        this.zoom = zoom;
        this.camX = camX;
        this.camZ = camZ;
    }

    public double zoom() {
        return zoom;
    }

    public double camX() {
        return camX;
    }

    public double camZ() {
        return camZ;
    }

    @Override
    public String toString() {
        return EventManager.SILENT_EVENT;
    }
}
