package core.gameinterface;

import api.Position;
import api.gameinterface.GameInterface;
import api.world.World;
import core.ImplPosition;

public class ConsoleInterface implements GameInterface {
    private final int[] displaySize = {10, 10};
    private final Position camPos;
    private final World world;

    public ConsoleInterface(World world) {
        this.world = world;
        this.camPos = new ImplPosition(world, displaySize[0] / 2, 0, displaySize[1] / 2);
    }

    @Override
    public void displayGame() {
        
    }

    @Override
    public boolean processInput() {
        return false;
    }
}
