package core;

import api.Position;
import api.world.World;
import core.entities.Occupant;

import java.util.Optional;

/**
 * The implementation of a Position
 */
public class ImplPosition implements Position {
    private int x;
    private int y;
    private int z;
    private World world;

    public ImplPosition(World world, int x, int y, int z) {
        this.world = world;
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
    public World getWorld() {
        return world;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public Optional<Occupant> getOccupant() {
        return world.getOccupant(this);
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
    public Position multiply(int x, int y, int z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    @Override
    public Position multiply(int m) {
        return multiply(m, m, m);
    }

    @Override
    public Position clone() {
        Position position;
        try {
            position = (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            position = new ImplPosition(world, x, y, z);
        }
        position.setX(x); position.setY(y); position.setZ(z);
        return position;
    }

    @Override
    public Position normalize() {
        x /= Math.abs(x);
        y /= Math.abs(y);
        z /= Math.abs(z);
        return this;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position pos)) return false;
        return pos.getX() == x && pos.getY() == y && pos.getZ() == z;
    }

    @Override
    public int compareTo(Position o) {
        if (x != o.getX()) return x - o.getX();
        if (y != o.getY()) return y - o.getY();
        return z - o.getZ();
    }
}
