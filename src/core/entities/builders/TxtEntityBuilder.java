package core.entities.builders;

import api.Direction;
import core.entities.Occupant;
import core.entities.instances.occupants.*;
import core.utils.builders.TxtBuilder;
import core.world.ImplWorld;

/**
 * A {@link TxtBuilder} that can build {@link Occupant}s
 */
public class TxtEntityBuilder extends TxtBuilder<Occupant> {
    public TxtEntityBuilder() {
        registerFromString("H", (chr, pos) -> new Human(pos));
        registerFromString("#", (chr, pos) -> new Border(pos));
        registerFromString(
            "B",
            (c, pos) -> new QuantumBox(pos, new ImplWorld(pos.getWorld().getEventManager()), 8)
        );
        registerFromString("/\\", (chr, pos) -> new RotatingPanel(pos, chr == '/'));
        registerFromString("t", (chr, pos) -> new Turntable(pos));
        registerFromString("Z", (chr, pos) -> new Zombie(pos, Direction.getNonZeroRandom(), 10));
    }
}
