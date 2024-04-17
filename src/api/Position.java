package api;

import api.world.World;
import core.entities.Occupant;

import java.util.Optional;

/**
 * An interface representing a position on a world
 */
public interface Position extends Cloneable, Comparable<Position> {
    /**
     * @return The x coordinate
     */
    int getX();

    /**
     * A setter for the x coordinate
     * @param x The new x coordinate
     */
    void setX(int x);

    /**
     * @return The y coordinate
     */
    int getY();

    /**
     * A setter for the y coordinate
     * @param y The new y coordinate
     */
    void setY(int y);

    /**
     * @return The z coordinate
     */
    int getZ();

    /**
     * A setter for the z coordinate
     * @param z The new z coordinate
     */
    void setZ(int z);

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

    /**
     * Adds each given component to this position
     * @param x The x to add
     * @param y The y to add
     * @param z The z to add
     * @return This position
     */
    Position add(int x, int y, int z);

    /**
     * Adds another position to this one
     * @param other The position to add
     * @return This position
     */
    Position add(Position other);

    /**
     * Multiplies each given component with this position
     * @param x The x multiplier
     * @param y The y multiplier
     * @param z The z multiplier
     * @return This position
     */
    Position multiply(int x, int y, int z);

    /**
     * Multiplies this position by the given number
     * @param m The multiplier
     * @return This position
     */
    Position multiply(int m);

    /**
     * @return A clone of this position
     */
    Position clone();

    /**
     * <p>Normalizes this position
     * <p>With integer positions, brings this position back into a cube of size 2, centered around (0, 0, 0)
     * (Coordinates going from (-1, -1, -1) to (1, 1, 1))
     * @return This position after being normalized
     */
    Position normalize();
}
