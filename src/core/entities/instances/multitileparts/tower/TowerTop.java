package core.entities.instances.multitileparts.tower;

import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.Redirector;
import core.entities.Moving;
import core.entities.MultiTilePart;
import core.entities.instances.multitiles.Tower;

import java.util.List;

public class TowerTop extends MultiTilePart<Tower> implements Redirector, ConsoleDisplayable {
    public TowerTop(Position position, Tower multiTile) {
        super(position, multiTile);
    }

    @Override
    public char toChar() {
        return 'T';
    }

    @Override
    public void redirect(Moving m) {
        getMultiTile().setOwner(m.getUniqueId());
        List<Tower> towers = getPosition().getWorld().getAllOfType(Tower.class);
        towers.removeIf(t -> t.getOwner() == null || !t.getOwner().equals(m.getUniqueId()));
        towers.removeIf(t -> t.getTop().getPosition().clone().add(0, -1, 0).getOccupant().isPresent());
        if (!towers.isEmpty() && Math.random() < 0.5) {
            Tower t = towers.get((int) (Math.random() * towers.size()));
            m.setPosition(t.getTop().getPosition().clone().add(0, -1, 0));
        }
        m.getDirection().multiply(-1);
    }
}
