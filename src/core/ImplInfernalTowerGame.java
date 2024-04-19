package core;

import api.InfernalTowerGame;
import api.entities.Ticking;
import api.gameinterface.GameInterface;
import api.world.World;
import core.entities.Occupant;
import core.entities.instances.Human;

public class ImplInfernalTowerGame implements InfernalTowerGame {
    private final GameInterface gameInterface;
    private final World world;
    private final float FRAME_RATE = 1;

    public ImplInfernalTowerGame(GameInterface gameInterface, World world) {
        this.gameInterface = gameInterface;
        this.world = world;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void startGame() {
        while (gameInterface.processInput()) {
            for (Occupant occupant : world.getOccupants()) {
                if (occupant instanceof Ticking ticking) ticking.tick();
            }

            gameInterface.displayGame();

            try {
                Thread.sleep((long) ((1 / FRAME_RATE) * 1000));
            } catch (InterruptedException ignored) {}
        }
    }
}
