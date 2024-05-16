package core.utils.display;

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

    public boolean canMove() {
        return canMove;
    }

    public double zoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        if (!canMove) return;
        this.zoom = zoom;
    }

    public double camX() {
        return camX;
    }

    public void setCamX(double camX) {
        if (!canMove) return;
        this.camX = camX;
    }

    public int camY() {
        return camY;
    }

    public void setCamY(int camY) {
        if (!canMove) return;
        this.camY = camY;
    }

    public double camZ() {
        return camZ;
    }

    public void setCamZ(double camZ) {
        if (!canMove) return;
        this.camZ = camZ;
    }
}
