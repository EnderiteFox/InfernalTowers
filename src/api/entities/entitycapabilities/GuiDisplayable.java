package api.entities.entitycapabilities;

import com.almasb.fxgl.entity.Entity;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

/**
 * An interface used for elements that can be displayed in the graphical interface
 */
public interface GuiDisplayable {
    /**
     * Initializes the displayable. This function should only be called once, when the Gui is initialized
     */
    default void initDisplayable() {}

    /**
     * @return the FXGL Entity of this element
     */
    Entity getEntity();

    /**
     * @return the FXGL Node used as a view for the entity of this element
     */
    ImageView getView();

    /**
     * Updates the node and entity
     * @param cameraState The current camera state
     */
    void updateNode(CameraState cameraState);
}
