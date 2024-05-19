package api.gameinterface;

/**
 * A {@link GameInterface} that can have inputs that have to be initialized
 */
public interface InputInterface extends GameInterface {
    /**
     * Initializes the inputs of this game interface
     */
    void initInput();
}
