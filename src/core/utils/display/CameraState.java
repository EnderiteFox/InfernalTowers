package core.utils.display;

/**
 * A class that can store the current state of a camera
 */
public class CameraState {
    private double zoom;
    private double camX;
    private int camY;
    private double camZ;
    private final boolean canMove;

    public CameraState(double zoom, double camX, int camY, double camZ, boolean canMove) {
        this.zoom = zoom;
        this.camX = camX;
        this.camY = camY;
        this.camZ = camZ;
        this.canMove = canMove;
    }

    /**
     * @return {@code true} if the camera can move, {@code false} otherwise
     */
    public boolean canMove() {
        return canMove;
    }

    /**
     * @return the zoom value of the camera
     */
    public double zoom() {
        return zoom;
    }

    /**
     * Sets the zoom value of the camera
     * @param zoom The new zoom value
     */
    public void setZoom(double zoom) {
        if (!canMove) return;
        this.zoom = zoom;
    }

    /**
     * @return the X coordinate of the camera
     */
    public double camX() {
        return camX;
    }

    /**
     * Sets the X coordinate of the camera
     * @param camX The new X coordinate
     */
    public void setCamX(double camX) {
        if (!canMove) return;
        this.camX = camX;
    }

    /**
     * @return the Y coordinate of the camera
     */
    public int camY() {
        return camY;
    }

    /**
     * Sets the Y coordinate of the camera
     * @param camY The new Y coordinate
     */
    public void setCamY(int camY) {
        if (!canMove) return;
        this.camY = camY;
    }

    /**
     * @return The Z coordinate of the camera
     */
    public double camZ() {
        return camZ;
    }

    /**
     * Sets the Z coordinate of the camera
     * @param camZ The new Z coordinate
     */
    public void setCamZ(double camZ) {
        if (!canMove) return;
        this.camZ = camZ;
    }
}
