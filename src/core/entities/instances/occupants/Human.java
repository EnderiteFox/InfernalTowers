package core.entities.instances.occupants;

import api.Direction;
import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.Redirector;
import core.entities.Moving;

/**
 * A human entity
 */
public class Human extends Moving implements Redirector, ConsoleDisplayable {
    public Human(Position position) {
        super(
            position,
            Direction.getNonZeroRandom()
        );
    }

    @Override
    public void redirect(Moving m) {
        m.getDirection().multiply(-1);
    }

    @Override
    public char toChar() {
        return 'H';
    }
}
