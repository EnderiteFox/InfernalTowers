package core.entities.instances.occupants;

import api.Direction;
import api.Position;
import api.entities.Building;
import api.entities.Ticking;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.Redirector;
import api.utils.CharGrid;
import api.world.World;
import core.ImplPosition;
import core.entities.Moving;
import core.entities.Occupant;
import core.gameinterface.ConsoleInterface;

import java.util.List;

public class RelativityBox extends Occupant implements Building, Ticking, ConsoleDisplayable, Redirector {
    private final World world;
    private final int width;
    private final int height;
    private ConsoleInterface consoleInterface = null;

    public RelativityBox(Position position, World world, int width, int height) {
        super(position);
        this.world = world;
        this.width = width;
        this.height = height;
    }

    public RelativityBox(Position position, World world, int size) {
        this(position, world, size, size);
    }

    public void enterBox(Moving m) {
        Direction dir = m.getDirection().clone().normalize();
        Position enterPos = new ImplPosition(
            world,
            dir.getX() < 0 ? width : dir.getX() == 0 ? width / 2 : 0,
            0,
            dir.getZ() < 0 ? height : dir.getZ() == 0 ? height / 2 : 0
        );
        if (enterPos.getOccupant().isPresent()) m.getDirection().multiply(-1);
        else m.setPosition(enterPos);
    }

    public void exitBox(Moving m) {
        Position exitPos = getPosition().clone().add(m.getDirection().clone().normalize());
        if (exitPos.getOccupant().isPresent()) m.getDirection().multiply(-1);
        else m.setPosition(exitPos);
    }

    @Override
    public boolean isOccupied() {
        if (world.getOccupants().stream().anyMatch(o -> o instanceof Moving)) return true;
        List<Building> buildings = world.getAllOfType(Building.class);
        if (buildings.isEmpty()) return false;
        return buildings.stream().anyMatch(Building::isOccupied);
    }

    @Override
    public List<Occupant> getOccupantsInside() {
        return world.getOccupants();
    }

    @Override
    public CharGrid getInsideView() {
        if (consoleInterface == null) consoleInterface = new ConsoleInterface(world);
        CharGrid worldGrid = consoleInterface.buildDisplayGrid();
        worldGrid.setChar(-1, -1, '/');
        worldGrid.setChar(-1, height + 1, '\\');
        worldGrid.setChar(width + 1, -1, '\\');
        worldGrid.setChar(width + 1, height + 1, '/');
        for (int i = 0; i <= width; ++i) {
            worldGrid.setChar(i, -1, '-');
            worldGrid.setChar(i, height + 1, '-');
        }
        for (int i = 0; i <= height; ++i) {
            worldGrid.setChar(-1, i, '|');
            worldGrid.setChar(width + 1, i, '|');
        }
        CharGrid buildings = consoleInterface.buildBuildingsGrid();
        if (buildings != null) worldGrid.addSidePanel(buildings);
        return worldGrid;
    }

    @Override
    public void tick() {
        world.tick();
        world.getAllOfType(Moving.class).forEach(m -> {if (isOut(m)) exitBox(m);});
    }

    private boolean isOut(Moving m) {
        Position pos = m.getPosition();
        return pos.getX() < 0 || pos.getX() >= width || pos.getZ() < 0 || pos.getZ() >= height;
    }

    @Override
    public char toChar() {
        return 'B';
    }

    @Override
    public void redirect(Moving m) {
        enterBox(m);
    }
}
