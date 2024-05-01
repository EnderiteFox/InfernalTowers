package core;

import api.InfernalTowerGame;
import api.gameinterface.GameInterface;
import api.world.World;

public class ImplInfernalTowerGame implements InfernalTowerGame {
    private final GameInterface gameInterface;
    private final World world;
    private final float frameRate;

    public ImplInfernalTowerGame(GameInterface gameInterface, World world, float frameRate) {
        this.gameInterface = gameInterface;
        this.world = world;
        this.frameRate = frameRate;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void startGame() {
        while (gameInterface.processInput()) {
            world.tick();

            gameInterface.displayGame();

            try {
                Thread.sleep((long) ((1 / frameRate) * 1000));
            } catch (InterruptedException ignored) {}
        }
    }
}
