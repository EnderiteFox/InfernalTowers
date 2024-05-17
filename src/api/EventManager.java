package api;

import api.events.Event;

import java.util.function.Consumer;

public interface EventManager {
    /**
     * <p>All events having a toString method returning this constant will be silenced in the debug logs.
     * <p>This is useful for events that are called very often and are spamming the debug logs while not being
     * very important
     */
    String SILENT_EVENT = "SILENT_EVENT";

    String WORLDLOAD_REGISTRY = "worldload_registry";
    String GUI_LOAD_REGISTRY = "gui_load_registry";

    <T extends Event> void registerListener(Class<T> clazz, String id, Consumer<T> listener);
    <T extends Event> void registerListener(Class<T> clazz, Consumer<T> listener);
    <T extends Event> void unregisterListener(Class<T> clazz, String id);
    <T extends Event> void callEvent(T event);

    /**
     * <p>Programs an event to be listened to later
     * <p>Events called that way are stored in a registry, and are listened to when the flush function is called with
     * the registry name the event is called with
     * <p>An example of this is deferring events called during world loading to listen to them when the game starts
     * @param event The event to call
     * @param <T> The type of the event to call
     */
    <T extends Event> void callDeferredEvent(String registryName, T event);

    /**
     * Executes the listeners for the deferred events with the given registry name, and deletes them from the registry
     * @param registryName The name of the registry to flush
     */
    void flushDeferredEvents(String registryName);
}
