package core.utils;

import java.util.function.Supplier;

/**
 * <p>A utility class used to load assets at the last moment
 * <p>When getting the asset using {@link DeferredAsset#get()}, if it was not loaded before, it is loaded using the given
 * {@link Supplier}. If it was already loaded, it is simply returned
 * @param <T> The type of the asset to load and get
 */
public class DeferredAsset<T> {
    private final Supplier<T> supplier;
    private T asset;
    private boolean loaded = false;

    public DeferredAsset(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Loads the asset if it was not loaded, and returns it
     * @return The loaded asset
     */
    public T get() {
        if (!loaded) {
            asset = supplier.get();
            loaded = true;
        }
        return asset;
    }

    /**
     * Updates the asset by calling the supplier once again
     */
    public void update() {
        asset = supplier.get();
        loaded = true;
    }


    /**
     * Rebuilds the asset by calling the supplier, but does not update the asset stored in this object
     * @return The result of the supplier
     */
    public T build() {
        return supplier.get();
    }
}
