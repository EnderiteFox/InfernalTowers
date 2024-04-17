import api.InfernalTowerGame;
import api.world.World;
import api.world.WorldLoader;
import core.ImplInfernalTowerGame;
import core.gameinterface.ConsoleInterface;
import core.world.loaders.TxtWorldLoader;

/**
 * The main class
 */
public class Main {
    /**
     * Reads command line arguments, and starts the game
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        WorldLoader worldLoader = new TxtWorldLoader();
        World world = worldLoader.loadWorld("src/testingWorld.txt");
        InfernalTowerGame game = new ImplInfernalTowerGame(
            new ConsoleInterface(world), world
        );
        game.startGame();
    }
}
