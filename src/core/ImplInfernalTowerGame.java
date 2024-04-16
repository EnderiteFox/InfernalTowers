package core;

import api.InfernalTowerGame;
import api.entities.Ticking;
import api.gameinterface.GameInterface;
import api.world.World;
import core.entities.Occupant;

public class ImplInfernalTowerGame implements InfernalTowerGame {
    private final GameInterface gameInterface;
    private final World world;
    private final float frameRate = 2;

    public ImplInfernalTowerGame(GameInterface gameInterface, World world) {
        this.gameInterface = gameInterface;
        this.world = world;
    }

    @Override
    public void startGame() {
        while (gameInterface.processInput()) {
            for (Occupant occupant : world.getOccupants()) {
                if (occupant instanceof Ticking ticking) ticking.tick();
            }

            gameInterface.displayGame();
            try {
                wait((long) ((1 / frameRate) * 1000));
            } catch (InterruptedException ignored) {
            }
        }
    }
}
