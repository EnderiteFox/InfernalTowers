package api.entities;

import core.utils.display.CameraState;

public interface GuiGlobalDisplayable {
    default void initDisplayable() {}

    CameraState getDefaultCameraState();

    void updateFrame(CameraState cameraState);

    void onEnterView();

    void onLeaveView();

    boolean isInView();

    void setInView(boolean inView);
}
