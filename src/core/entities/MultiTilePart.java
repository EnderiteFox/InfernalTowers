package core.entities;

import api.Position;

public abstract class MultiTilePart extends Occupant {
    private final MultiTile multiTile;
    public MultiTilePart(Position position, MultiTile multiTile) {
        super(position);
        this.multiTile = multiTile;
    }

    public MultiTile getMultiTile() {
        return multiTile;
    }
}
