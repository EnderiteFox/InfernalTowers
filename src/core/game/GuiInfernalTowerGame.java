package core.game;

import api.EventManager;
import api.InfernalTowerGame;
import api.gameinterface.GameInterface;
import api.gameinterface.InputInterface;
import api.world.World;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class GuiInfernalTowerGame extends GameApplication implements InfernalTowerGame {
    private static GameInterface gameInterface;
    private static World world;
    private static float frameRate;
    private static long lastTick = 0;
    private static boolean firstTick = true;

    public GuiInfernalTowerGame() {}

    public GuiInfernalTowerGame(GameInterface gameInterface, World world, float frameRate) {
        GuiInfernalTowerGame.gameInterface = gameInterface;
        GuiInfernalTowerGame.world = world;
        GuiInfernalTowerGame.frameRate = frameRate;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(700);
        settings.setWidth(900);
        settings.setTitle("Infernal Towers");
        settings.setVersion("0.1");
        settings.setGameMenuEnabled(false);
    }

    @Override
    protected void initInput() {
        super.initInput();
        if (gameInterface instanceof InputInterface input) input.initInput();
    }

    @Override
    public void startGame() {
        world.getEventManager().flushDeferredEvents(EventManager.WORLDLOAD_REGISTRY);
        GuiInfernalTowerGame.main(new String[0]);
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
        if (firstTick) {
            world.getEventManager().flushDeferredEvents(EventManager.GUI_LOAD_REGISTRY);
            firstTick = false;
        }
        if (System.currentTimeMillis() - lastTick >= (1 / frameRate) * 1000) {
            lastTick = System.currentTimeMillis();
            world.tick();
        }
        gameInterface.displayGame();
    }
}
