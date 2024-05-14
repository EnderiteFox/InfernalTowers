package core.events;

import api.EventManager;
import api.events.Event;

import java.util.*;
import java.util.function.Consumer;

public class ImplEventManager implements EventManager {
    private final boolean debugMode;

    private final Map<Class<? extends Event>, Map<String, Consumer<? extends Event>>> listeners = new HashMap<>();
    private final Map<String, List<? extends Event>> deferredRegistries = new HashMap<>();

    public ImplEventManager(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public <T extends Event> void registerListener(Class<T> clazz, String id, Consumer<T> listener) {
        listeners.putIfAbsent(clazz, new HashMap<>());
        listeners.get(clazz).put(id, listener);
    }

    @Override
    public <T extends Event> void registerListener(Class<T> clazz, Consumer<T> listener) {
        registerListener(clazz, UUID.randomUUID().toString(), listener);
    }

    @Override
    public <T extends Event> void unregisterListener(Class<T> clazz, String id) {
        if (listeners.containsKey(clazz)) listeners.get(clazz).remove(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void callEvent(T event) {
        if (debugMode && !event.toString().equals(EventManager.SILENT_EVENT)) System.out.println(event);
        if (!listeners.containsKey(event.getClass())) return;
        listeners.get(event.getClass()).values().forEach(c -> ((Consumer<T>) c).accept(event));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void callDeferredEvent(String registryName, T event) {
        deferredRegistries.putIfAbsent(registryName, new ArrayList<>());
        ((List<T>) deferredRegistries.get(registryName)).add(event);
    }

    @Override
    public void flushDeferredEvents(String registryName) {
        deferredRegistries.putIfAbsent(registryName, new ArrayList<>());
        deferredRegistries.get(registryName).forEach(this::callEvent);
        deferredRegistries.get(registryName).clear();
    }
}
