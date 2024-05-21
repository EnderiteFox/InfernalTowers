package core.gameinterface;

import api.entities.Building;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.gameinterface.GameInterface;
import api.utils.CharGrid;
import api.world.World;
import core.entities.Occupant;
import core.utils.ImplCharGrid;

import java.util.List;

/**
 * The implementation of a GameInterface, that uses the console to display the game
 */
public class ConsoleInterface implements GameInterface {
    private final World world;

    public ConsoleInterface(World world) {
        this.world = world;
    }

    @Override
    public void displayGame() {
        System.out.println();
        CharGrid grid = buildDisplayGrid();
        CharGrid buildings = buildBuildingsGrid();
        if (buildings != null) grid.addSidePanel(buildings);
        System.out.println(grid);
    }

    /**
     * @return a {@link CharGrid} displaying the main world
     */
    public CharGrid buildDisplayGrid() {
        CharGrid charGrid = new ImplCharGrid();
        for (Occupant occupant : world.getOccupants()) {
            if (!(occupant instanceof ConsoleDisplayable)) continue;
            ConsoleDisplayable displayable = (ConsoleDisplayable) occupant;
            charGrid.setChar(
                occupant.getPosition().getX(),
                occupant.getPosition().getZ(),
                displayable.toChar()
            );
        }
        return charGrid;
    }

    /**
     * @return a {@link CharGrid} representing the inside view of all occupied buildings
     */
    public CharGrid buildBuildingsGrid() {
        CharGrid buildingsView = null;
        List<Building> buildings = world.getAllOfType(Building.class);
        for (Building b : buildings) {
            if (!b.isOccupied()) continue;
            if (buildingsView == null) buildingsView = b.getInsideView();
            else buildingsView.addSidePanel(' ', CharGrid.SidePanelDirection.RIGHT, b.getInsideView());
        }
        return buildingsView;
    }

    @Override
    public boolean processInput() {
        return true;
    }
}
