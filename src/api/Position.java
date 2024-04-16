package api;

import api.world.World;
import core.entities.Occupant;

import java.util.Optional;

public interface Position extends Cloneable {
    int getX();
    void setX(int x);
    int getY();
    void setY(int y);
    int getZ();
    void setZ(int z);
    World getWorld();
    void setWorld(World world);
    Optional<Occupant> getOccupant();
    Position add(int x, int y, int z);
    Position add(Position other);
    Position multiply(int x, int y, int z);
    Position multiply(int m);
    Position clone();
    Position normalize();
}
