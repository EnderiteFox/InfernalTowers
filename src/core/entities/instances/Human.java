package core.entities.instances;

import api.Position;
import api.entities.Redirector;
import core.entities.Moving;

public class Human extends Moving implements Redirector {
    public Human(Position position, Position direction) {
        super(position, direction);
    }

    @Override
    public void redirect(Moving m) {
        m.getDirection().multiply(-1);
    }
}
