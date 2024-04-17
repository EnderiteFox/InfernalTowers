package core.gameinterface;

import api.entities.ConsoleDisplayable;
import api.gameinterface.GameInterface;
import api.utils.CharGrid;
import api.world.World;
import core.entities.Occupant;
import core.utils.ImplCharGrid;

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
        System.out.println(charGrid);
    }

    @Override
    public boolean processInput() {
        return true;
    }
}
