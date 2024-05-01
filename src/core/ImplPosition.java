package core;

import api.Direction;
import api.Position;
import api.world.World;
import core.entities.Occupant;

import java.util.Optional;

/**
 * The implementation of a Position
 */
public class ImplPosition extends ImplDirection implements Position {
    private World world;

    public ImplPosition(World world, int x, int y, int z) {
        super(x, y, z);
        this.world = world;
    }

    @Override
    public Position add(int x, int y, int z) {
        super.add(x, y, z);
        return this;
    }

    @Override
    public Position add(Direction other) {
        super.add(other);
        return this;
    }

    @Override
    public Position multiply(int x, int y, int z) {
        super.multiply(x, y, z);
        return this;
    }

    @Override
    public Position multiply(int m) {
        super.multiply(m);
        return this;
    }

    @Override
    public Position normalize() {
        super.normalize();
        return this;
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
    public Position clone() {
        Position position;
        position = (Position) super.clone();
        position.setX(x); position.setY(y); position.setZ(z);
        return position;
    }
}
