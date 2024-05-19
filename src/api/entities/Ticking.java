package api.entities;

/**
 * An interface representing an entity that executes an action every game tick
 */
public interface Ticking {
    /**
     * Execute all the actions required for this game tick. Called by the game manager
     */
    void tick();
}
