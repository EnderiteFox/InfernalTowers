package core.entities.instances;

import api.Position;
import api.entities.Redirector;
import core.entities.Moving;
import core.entities.Occupant;

public class Border extends Occupant implements Redirector {
    public Border(Position position) {
        super(position);
    }

    @Override
    public void redirect(Moving m) {
        
    }
}
