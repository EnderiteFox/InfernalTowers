package core.entities.instances.multitileparts.tower;

import api.Position;
import api.entities.entitycapabilities.Redirector;
import core.ImplPosition;
import core.entities.Moving;
import core.entities.MultiTilePart;
import core.entities.instances.multitiles.Tower;

import java.util.ArrayList;
import java.util.List;

public class TowerEntrance extends MultiTilePart<Tower> implements Redirector {
    public TowerEntrance(Position position, Tower multiTile) {
        super(position, multiTile);
    }

    @Override
    public void redirect(Moving m) {
        if (m.getDirection().getY() < 0) {
            List<Position> exitDirections = new ArrayList<>();
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    Position pos = new ImplPosition(getPosition().getWorld(), x, 0, z);
                    if (getPosition().clone().add(pos).getOccupant().isEmpty()) exitDirections.add(pos);
                }
            }
            if (exitDirections.isEmpty()) return;
            Position exitDir = exitDirections.get((int) (Math.random() * exitDirections.size()));
            m.setPosition(getPosition().clone().add(exitDir));
            m.setDirection(exitDir);
        }
        else {
            if (getPosition().clone().add(0, 1, 0).getOccupant().isPresent()) m.getDirection().multiply(-1);
            else {
                m.setPosition(getPosition().clone().add(0, 1, 0));
                m.setDirection(new ImplPosition(getPosition().getWorld(), 0, 1, 0));
            }
        }
    }
}
