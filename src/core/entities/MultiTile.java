package core.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a structure composed of multiple occupants
 */
public abstract class MultiTile {
    protected final List<Occupant> occupants = new ArrayList<>();

    public List<Occupant> getOccupants() {
        return occupants;
    }
}
