package core.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class representing a structure composed of multiple occupants
 */
public abstract class MultiTile {
    protected final List<Occupant> occupants = new ArrayList<>();

    public MultiTile(Collection<Occupant> occupants) {
        this.occupants.addAll(occupants);
    }

    public MultiTile(Occupant... occupants) {
        this(List.of(occupants));
    }

    public List<Occupant> getOccupants() {
        return occupants;
    }
}
