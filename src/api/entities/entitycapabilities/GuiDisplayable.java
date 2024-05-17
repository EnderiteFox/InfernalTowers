package api.entities.entitycapabilities;

import com.almasb.fxgl.entity.Entity;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

public interface GuiDisplayable {
    /**
     * Initializes the displayable. This function should only be called once, when the Gui is initialized
     */
    default void initDisplayable() {}

    Entity getEntity();

    ImageView getView();

    void updateNode(CameraState cameraState);
}
