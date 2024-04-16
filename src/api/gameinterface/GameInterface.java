package api.gameinterface;

/**
 * An interface representing an interface for the game
 */
public interface GameInterface {
    /**
     * Displays a game frame
     */
    void displayGame();

    /**
     * Processes any input for the game
     * @return {@code false} if a game ending input has been detected, {@code true} otherwise
     */
    boolean processInput();
}
