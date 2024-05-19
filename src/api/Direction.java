package api;

import core.ImplDirection;

/**
 * An interface representing a direction, having x, y and z coordinates
 */
public interface Direction extends Cloneable, Comparable<Direction> {
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
     * Adds each given component to this position
     * @param x The x to add
     * @param y The y to add
     * @param z The z to add
     * @return This position
     */
    Direction add(int x, int y, int z);

    /**
     * Adds another position to this one
     * @param other The position to add
     * @return This position
     */
    Direction add(Direction other);

    /**
     * Multiplies each given component with this position
     * @param x The x multiplier
     * @param y The y multiplier
     * @param z The z multiplier
     * @return This position
     */
    Direction multiply(int x, int y, int z);

    /**
     * Multiplies this position by the given number
     * @param m The multiplier
     * @return This position
     */
    Direction multiply(int m);

    /**
     * @return A clone of this position
     */
    Direction clone();

    /**
     * <p>Normalizes this position
     * <p>With integer positions, brings this position back into a cube of size 2, centered around (0, 0, 0)
     * (Coordinates going from (-1, -1, -1) to (1, 1, 1))
     * @return This position after being normalized
     */
    Direction normalize();

    /**
     * @return a random direction vector, with a maximum speed of 1 in each axis, and that is non-zero
     */
    static Direction getNonZeroRandom() {
        Direction dir = new ImplDirection(0, 0, 0);
        int rand = (int) (Math.random() * 8);
        dir.setX(
            switch (rand) {
                case 0, 1, 2 -> -1;
                case 4, 5, 6 -> 1;
                default -> 0;
            }
        );
        dir.setZ(
            switch (rand) {
                case 2, 3, 4 -> 1;
                case 0, 6, 7 -> -1;
                default -> 0;
            }
        );
        return dir;
    }
}
