package core.gameinterface;

import api.entities.multitilecapabilities.Building;
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
        CharGrid charGrid = new ImplCharGrid();
        for (Occupant occupant : world.getOccupants()) {
            if (!(occupant instanceof ConsoleDisplayable displayable)) continue;
            charGrid.setChar(
                occupant.getPosition().getX(),
                occupant.getPosition().getZ(),
                displayable.toChar()
            );
        }
        CharGrid buildingsView = null;
        List<Building> buildings = world.getAllOfType(Building.class);
        for (Building b : buildings) {
            if (!b.isOccupied()) continue;
            if (buildingsView == null) buildingsView = b.getInsideView();
            else buildingsView.addSidePanel(' ', CharGrid.SidePanelDirection.RIGHT, b.getInsideView());
        }
        if (buildingsView != null) charGrid.addSidePanel(buildingsView);
        System.out.println(charGrid);
    }

    @Override
    public boolean processInput() {
        return true;
    }
}
