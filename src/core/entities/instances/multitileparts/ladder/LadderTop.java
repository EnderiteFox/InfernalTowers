package core.entities.instances.multitileparts.ladder;

import api.Direction;
import api.Position;
import api.entities.entitycapabilities.Redirector;
import core.ImplDirection;
import core.entities.instances.multitiles.Ladder;

public class LadderTop extends LadderBottom implements Redirector {
    public LadderTop(Position position, Ladder multiTile) {
        super(position, multiTile);
    }

    @Override
    protected Direction getRedirectDirection() {
        return new ImplDirection(0, -1, 0);
    }
}
