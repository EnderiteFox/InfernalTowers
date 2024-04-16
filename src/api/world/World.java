package api.world;

import api.Position;
import core.entities.Occupant;

import java.util.List;
import java.util.Optional;

/**
 * An interface representing a world, containing multiple entities
 */
public interface World {
    List<Occupant> getOccupants();
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
}
