package core;

import api.Position;

public class ImplPosition implements Position {
    private int x;
    private int y;
    private int z;

    public ImplPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public Position add(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public Position add(Position other) {
        add(other.getX(), other.getY(), other.getZ());
        return this;
    }

    @Override
    public Position copy() {
        return new ImplPosition(x, y, z);
    }

    @Override
    public Position normalize() {
        x /= Math.abs(x);
        y /= Math.abs(y);
        z /= Math.abs(z);
        return this;
    }
}
