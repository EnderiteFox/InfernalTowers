package core.entities.instances.multitiles;

import api.Position;
import core.entities.MultiTile;
import core.entities.instances.multitileparts.ladder.LadderBottom;
import core.entities.instances.multitileparts.ladder.LadderTop;

/**
 * A ladder that can help occupants climb heights
 */
public class Ladder extends MultiTile {
    public Ladder(Position pos, int height) {
        assert height >= 1;
        occupants.add(new LadderBottom(pos, this));
        occupants.add(new LadderTop(pos.clone().add(0, 1 + height, 0), this));
    }
}
