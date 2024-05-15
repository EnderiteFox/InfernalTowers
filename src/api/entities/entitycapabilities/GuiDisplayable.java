package api.entities.entitycapabilities;

import com.almasb.fxgl.entity.Entity;
import core.utils.display.CameraState;

public interface GuiDisplayable {
    Entity getEntity();

    void updateNode(CameraState cameraState);
}
