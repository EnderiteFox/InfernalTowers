package core.game;

import api.EventManager;
import api.InfernalTowerGame;
import api.gameinterface.GameInterface;
import api.world.World;

/**
 * The implementation of the Infernal Towers game for consoles
 */
public class ConsoleInfernalTowerGame implements InfernalTowerGame {
    private final GameInterface gameInterface;
    private final World world;
    private final float frameRate;

    public ConsoleInfernalTowerGame(GameInterface gameInterface, World world, float frameRate) {
        this.gameInterface = gameInterface;
        this.world = world;
        this.frameRate = frameRate;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void startGame() {
        world.getEventManager().flushDeferredEvents(EventManager.WORLDLOAD_REGISTRY);
        while (gameInterface.processInput()) {
            world.tick();

            gameInterface.displayGame();

            try {
                Thread.sleep((long) ((1 / frameRate) * 1000));
            } catch (InterruptedException ignored) {}
        }
    }
}
