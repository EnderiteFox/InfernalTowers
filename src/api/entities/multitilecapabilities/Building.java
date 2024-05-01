package api.entities.multitilecapabilities;

import api.utils.CharGrid;
import core.entities.Occupant;

import java.util.List;

/**
 * An interface describing a building, a special MultiTile that can be displayed in a special way in order to show what
 * is happening inside
 */
public interface Building {
    /**
     * @return {@code true} if an Occupant is inside this building
     */
    boolean isOccupied();

    /**
     * @return A list of Occupants that are present inside the building
     */
    List<Occupant> getOccupantsInside();

    /**
     * @return An inside view of the building, as a CharGrid
     */
    CharGrid getInsideView();
}
