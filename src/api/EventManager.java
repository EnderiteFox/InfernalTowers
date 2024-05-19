package api;

import api.events.Event;

import java.util.function.Consumer;

/**
 * An interface representing an event manager, that can call events and register listeners for these events
 */
public interface EventManager {
    /**
     * <p>All events having a toString method returning this constant will be silenced in the debug logs.
     * <p>This is useful for events that are called very often and are spamming the debug logs while not being
     * very important
     */
    String SILENT_EVENT = "SILENT_EVENT";

    String WORLDLOAD_REGISTRY = "worldload_registry";
    String GUI_LOAD_REGISTRY = "gui_load_registry";

    /**
     * Registers a listener for an event. This listener will be run when the event is called
     * @param clazz The class of the event to register the listener for
     * @param id The id of the listener to register. If a listener is registered with the same id as an existing listener,
     *           the existing listener will be replaced with the new one
     * @param listener The listener to register
     * @param <T> The type of the event to register the listener for
     */
    <T extends Event> void registerListener(Class<T> clazz, String id, Consumer<T> listener);

    /**
     * An overload of {@link EventManager#registerListener(Class, String, Consumer)} that uses a random id for the listener
     * @param clazz The class of the event to register the listener for
     * @param listener The listener to register
     * @param <T> The type of the event to register the listener for
     */
    <T extends Event> void registerListener(Class<T> clazz, Consumer<T> listener);

    /**
     * Unregisters the listener with the given id
     * @param clazz The class of the event to unregister the listener for
     * @param id The id of the listener
     * @param <T> The type of the event to unregister the listener for
     */
    <T extends Event> void unregisterListener(Class<T> clazz, String id);

    /**
     * Calls an event and applies the listeners that are registered for it
     * @param event The event to call
     * @param <T> The type of the event to call
     */
    <T extends Event> void callEvent(T event);

    /**
     * <p>Stores an event to be listened to later
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
