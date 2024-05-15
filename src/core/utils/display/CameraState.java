package core.utils.display;

public class CameraState {
    private double zoom;
    private double camX;
    private int camY;
    private double camZ;

    public CameraState(double zoom, double camX, int camY, double camZ) {
        this.zoom = zoom;
        this.camX = camX;
        this.camY = camY;
        this.camZ = camZ;
    }

    public double zoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double camX() {
        return camX;
    }

    public void setCamX(double camX) {
        this.camX = camX;
    }

    public int camY() {
        return camY;
    }

    public void setCamY(int camY) {
        this.camY = camY;
    }

    public double camZ() {
        return camZ;
    }

    public void setCamZ(double camZ) {
        this.camZ = camZ;
    }
}
