package core.entities;

import api.Position;

public abstract class MultiTilePart<T> extends Occupant {
    private final T multiTile;
    public MultiTilePart(Position position, T multiTile) {
        super(position);
        this.multiTile = multiTile;
    }

    public T getMultiTile() {
        return multiTile;
    }
}
