package api.entities.entitycapabilities;

import core.entities.Moving;

/**
 * An interface representing an object that can redirect moving entities
 */
public interface Redirector {
    /**
     * Redirects the given moving entity
     * @param m The moving entity to redirect
     */
    void redirect(Moving m);
}
