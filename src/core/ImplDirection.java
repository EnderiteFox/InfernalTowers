package core;

import api.Direction;

public class ImplDirection implements Direction {
    protected int x, y, z;

    public ImplDirection(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public Direction add(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public Direction add(Direction other) {
        add(other.getX(), other.getY(), other.getZ());
        return this;
    }

    @Override
    public Direction multiply(int x, int y, int z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    @Override
    public Direction multiply(int m) {
        return multiply(m, m, m);
    }

    @Override
    public Direction clone() {
        Direction direction;
        try {
            direction = (Direction) super.clone();
        } catch (CloneNotSupportedException e) {
            direction = new ImplDirection(x, y, z);
        }
        direction.setX(x); direction.setY(y); direction.setZ(z);
        return direction;
    }

    @Override
    public Direction normalize() {
        if (x != 0) x /= Math.abs(x);
        if (y != 0) y /= Math.abs(y);
        if (z != 0) z /= Math.abs(z);
        return this;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Direction)) return false;
        Direction dir = (Direction) obj;
        return dir.getX() == x && dir.getY() == y && dir.getZ() == z;
    }

    @Override
    public int compareTo(Direction o) {
        if (x != o.getX()) return x - o.getX();
        if (z != o.getZ()) return z - o.getZ();
        return y - o.getY();
    }
}
