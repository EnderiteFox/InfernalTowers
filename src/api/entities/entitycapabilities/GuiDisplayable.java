package api.entities.entitycapabilities;

import com.almasb.fxgl.entity.Entity;
import javafx.scene.Node;

public interface GuiDisplayable {
    Entity getEntity();

    void updateNode(double zoom, double camX, double camZ);
}
