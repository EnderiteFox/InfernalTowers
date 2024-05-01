package core.entities.instances.tiles;

import api.Position;
import api.entities.ConsoleDisplayable;
import api.entities.Redirector;
import core.ImplPosition;
import core.entities.Moving;

/**
 * A human entity
 */
public class Human extends Moving implements Redirector, ConsoleDisplayable {
    public Human(Position position) {
        super(
            position,
            new ImplPosition(
                position.getWorld(),
                ((int) (Math.random() * 3)) - 1,
                0,
                ((int) (Math.random() * 3)) - 1
            )
        );
        while (getDirection().equals(new ImplPosition(position.getWorld(), 0, 0, 0))) {
            setDirection(
                new ImplPosition(
                    position.getWorld(),
                    ((int) (Math.random() * 3)) - 1,
                    0,
                    ((int) (Math.random() * 3)) - 1
                )
            );
        }
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
