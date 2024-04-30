package core.entities.instances;

import api.Position;
import api.entities.ConsoleDisplayable;
import api.entities.Redirector;
import core.entities.Moving;
import core.entities.Occupant;

/**
 * A Border entity, making other entities bounce back
 */
public class Border extends Occupant implements Redirector, ConsoleDisplayable {
    public Border(Position position) {
        super(position);
    }

    @Override
    public void redirect(Moving m) {
        Position pos = m.getPosition();
        if (m.getDirection().getY() != 0) m.getDirection().setY(0);
        if (m.getDirection().getX() == 0 || m.getDirection().getZ() == 0) m.getDirection().multiply(-1);
        else {
             Position xPos = pos.clone().add(m.getDirection().getX(), 0, 0);
             Position zPos = pos.clone().add(0, 0, m.getDirection().getZ());
             if (xPos.getOccupant().isEmpty() && zPos.getOccupant().isPresent()) {
                 pos = xPos;
                 m.getDirection().multiply(1, 1, -1);
             }
             else if (xPos.getOccupant().isPresent() && zPos.getOccupant().isEmpty()) {
                 pos = zPos;
                 m.getDirection().multiply(-1, 1, 1);
             }
             else m.getDirection().multiply(-1);
        }
        m.setPosition(pos);
    }

    @Override
    public char toChar() {
        return '#';
    }
}
