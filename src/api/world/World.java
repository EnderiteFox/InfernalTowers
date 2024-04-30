package api.world;

import api.Position;
import api.entities.Ticking;
import core.entities.MultiTile;
import core.entities.Occupant;

import java.util.List;
import java.util.Optional;

/**
 * An interface representing a world, containing multiple entities
 */
public interface World extends Ticking {
    /**
     * @return The occupants present in this world
     */
    List<Occupant> getOccupants();

    /**
     * @return The multitiles present in this world
     */
    List<MultiTile> getMultiTiles();

    void addMultiTile(MultiTile multiTile);

    void removeMultiTile(MultiTile multiTile);

    /**
     * Gets the occupant at the given position
     * @param pos The position of the tile to get from
     * @return An optional of the occupant present on the tile
     */
    Optional<Occupant> getOccupant(Position pos);

    /**
     * Sets the occupant at the given position
     * @param pos The position of the occupant
     * @param occupant The occupant to place on the tile
     */
    void setOccupant(Position pos, Occupant occupant);

    /**
     * Adds an Occupant to the world, using its position
     * @param occupant The Occupant to add to the world
     */
    void addOccupant(Occupant occupant);

    /**
     * Removes the occupant at the given Position
     * @param pos The Position to remove an occupant from
     */
    void removeOccupant(Position pos);

    /**
     * Removes the given Occupant from the world
     * @param occupant The Occupant to remove
     */
    void removeOccupant(Occupant occupant);
}
