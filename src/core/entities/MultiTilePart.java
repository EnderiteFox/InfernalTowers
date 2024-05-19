package core.entities;

import api.Position;

/**
 * An abstract class used for parts of a {@link MultiTile}
 * @param <T> The type of multitile this entity is part of
 */
public abstract class MultiTilePart<T> extends Occupant {
    private final T multiTile;

    public MultiTilePart(Position position, T multiTile) {
        super(position);
        this.multiTile = multiTile;
    }

    /**
     * @return the {@link MultiTile} this is part of
     */
    public T getMultiTile() {
        return multiTile;
    }
}
