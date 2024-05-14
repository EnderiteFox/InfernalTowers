package core.entities.instances.multitiles;

import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.Building;
import api.events.towers.CaptureTowerEvent;
import api.utils.CharGrid;
import api.world.World;
import core.entities.MultiTile;
import core.entities.Occupant;
import core.entities.instances.multitileparts.tower.TowerEntrance;
import core.entities.instances.multitileparts.tower.TowerTop;
import core.utils.ImplCharGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tower extends MultiTile implements Building {
    private UUID owner = null;
    private final int size;

    public Tower(Position pos) {
        this(pos, (int) (Math.random() * 9) + 1);
    }

    public Tower(Position pos, int size) {
        assert size >= 1;
        this.size = size;
        occupants.add(new TowerEntrance(pos, this));
        occupants.add(new TowerTop(pos.clone().add(0, 1 + size, 0), this));
    }

    public TowerTop getTop() {
        for (Occupant o : getOccupants()) if (o instanceof TowerTop t) return t;
        return null;
    }

    public TowerEntrance getEntrance() {
        for (Occupant o : getOccupants()) if (o instanceof TowerEntrance t) return t;
        return null;
    }

    @Override
    public List<Occupant> getOccupantsInside() {
        List<Occupant> occupants = new ArrayList<>();
        Position pos = getEntrance().getPosition().clone().add(0, 1, 0);
        while (
            (pos.getOccupant().isEmpty() || !getOccupants().contains(pos.getOccupant().get()))
                && pos.getZ() - getEntrance().getPosition().getZ() + 1 < size
        ) {
            pos.getOccupant().ifPresent(occupants::add);
            pos.add(0, 1, 0);
        }
        return occupants;
    }

    @Override
    public boolean isOccupied() {
        return !getOccupantsInside().isEmpty();
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        if (!this.owner.equals(owner)) {
            World world = getTop().getPosition().getWorld();
            world.getOccupant(owner).ifPresent(
                newO -> world.getOccupant(this.owner).ifPresent(
                    oldO -> world.getEventManager().callEvent(new CaptureTowerEvent(this, newO, oldO))
                )
            );
        }
        this.owner = owner;
    }

    @Override
    public CharGrid getInsideView() {
        CharGrid grid = new ImplCharGrid();
        grid.setChar(0, 0, '|');
        grid.setChar(1, 0, '#');
        grid.setChar(2, 0, '|');
        for (int i = 0; i < size; ++i) {
            grid.setChar(0, -1 - i, '|');
            grid.setChar(2, -1 - i, '|');
        }
        List<Occupant> inside = getOccupantsInside();
        inside.forEach(
            o -> {
                if (!(o instanceof ConsoleDisplayable displayable)) return;
                grid.setChar(
                    1,
                    getEntrance().getPosition().getY() - o.getPosition().getY(),
                    displayable.toChar()
                );
            }
        );
        grid.insertString(0, -size - 1, "/ \\");
        grid.setChar(1, -size - 2, '^');
        return grid;
    }
}
