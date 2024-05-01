package api;

import api.world.World;
import core.entities.Occupant;

import java.util.Optional;

/**
 * An interface representing a position on a world
 */
public interface Position extends Direction {
    @Override
    Position add(int x, int y, int z);

    @Override
    Position add(Direction other);

    @Override
    Position multiply(int x, int y, int z);

    @Override
    Position multiply(int m);

    /**
     * @return The world of this position
     */
    World getWorld();

    /**
     * A setter for the world of this position
     * @param world The new world
     */
    void setWorld(World world);

    /**
     * @return The occupant that is present at this position
     */
    Optional<Occupant> getOccupant();

    @Override
    Position clone();

    @Override
    Position normalize();
}
