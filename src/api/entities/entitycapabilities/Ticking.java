package api.entities.entitycapabilities;

/**
 * An interface representing an entity that executes an action every game tick
 */
public interface Ticking {
    /**
     * Execute all the actions required this game tick. Called by the game manager
     */
    void tick();
}
