package api.entities;

import core.utils.display.CameraState;

public interface GuiGlobalDisplayable {
    CameraState getDefaultCameraState();

    void updateFrame(CameraState cameraState);

    void onEnterView();

    void onLeaveView();
}
