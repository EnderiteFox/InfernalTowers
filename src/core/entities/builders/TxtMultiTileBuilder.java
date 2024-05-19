package core.entities.builders;

import core.entities.MultiTile;
import core.entities.instances.multitiles.Tower;
import core.utils.builders.TxtBuilder;

/**
 * A {@link TxtBuilder} that can build {@link MultiTile}s
 */
public class TxtMultiTileBuilder extends TxtBuilder<MultiTile> {
    public TxtMultiTileBuilder() {
        builderMap.put(
            c -> (c >= '1' && c <= '9') || c == 'T',
            (c, pos) -> c == 'T' ? new Tower(pos) : new Tower(pos, c - '0')
        );
    }
}
