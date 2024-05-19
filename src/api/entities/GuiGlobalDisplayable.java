package api.entities;

import core.utils.display.CameraState;

/**
 * An interface used for elements that are used as a main display for the graphical interface. These elements are
 * generally clickable elements that can display a view of what is inside of them
 */
public interface GuiGlobalDisplayable {
    /**
     * Initialized the displayable. Event listeners are generally registered here
     */
    default void initDisplayable() {}

    /**
     * @return the default camera state that is used when entering the view of the displayable
     */
    CameraState getDefaultCameraState();

    /**
     * Updates a frame for the displayable
     * @param cameraState The current camera state
     */
    void updateFrame(CameraState cameraState);

    /**
     * Called when the displayable starts to be displayed
     */
    void onEnterView();

    /**
     * Called when the displayable stops being displayed
     */
    void onLeaveView();

    /**
     * @return {@code true} if the displayable is currently displayed, {@code false} otherwise
     */
    boolean isInView();

    /**
     * Changes the displayed state of the displayable
     * @param inView {@code true} if the displayable is now displayed, {@code false} otherwise
     */
    void setInView(boolean inView);
}
