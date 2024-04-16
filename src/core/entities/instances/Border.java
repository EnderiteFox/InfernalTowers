package core.entities.instances;

import api.Position;
import api.entities.Redirector;
import api.world.World;
import core.entities.Moving;
import core.entities.Occupant;

/**
 * A Border entity, making other entities bounce back
 */
public class Border extends Occupant implements Redirector {
    public Border(Position position, World world) {
        super(position);
    }

    @Override
    public void redirect(Moving m) {
        Position pos = m.getPosition();
        if (pos.getZ() != 0) m.getPosition().setZ(0);
        if (pos.getX() == 0 || pos.getZ() == 0) m.getPosition().multiply(-1);
        else {
             Position xPos = pos.clone().add(m.getDirection().getX(), 0, 0);
             Position zPos = pos.clone().add(0, 0 ,m.getDirection().getZ());
             if (xPos.getOccupant().isEmpty() && zPos.getOccupant().isPresent()) {
                 m.getPosition().add(m.getDirection().getX(), 0, 0);
                 m.getDirection().multiply(1, 1, -1);
             }
             else if (xPos.getOccupant().isPresent() && zPos.getOccupant().isEmpty()) {
                 m.getPosition().add(0, 0, m.getDirection().getZ());
                 m.getDirection().multiply(-1, 1, 1);
             }
             else m.getDirection().multiply(-1);
        }
    }
}
