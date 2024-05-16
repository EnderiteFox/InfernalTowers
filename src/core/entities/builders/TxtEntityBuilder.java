package core.entities.builders;

import core.entities.Occupant;
import core.entities.instances.occupants.Border;
import core.entities.instances.occupants.Human;
import core.entities.instances.occupants.QuantumBox;
import core.utils.builders.TxtBuilder;
import core.world.ImplWorld;

public class TxtEntityBuilder extends TxtBuilder<Occupant> {
    public TxtEntityBuilder() {
        registerFromString("H", (chr, pos) -> new Human(pos));
        registerFromString("#", (chr, pos) -> new Border(pos));
        registerFromString(
            "B",
            (c, pos) -> new QuantumBox(pos, new ImplWorld(pos.getWorld().getEventManager()), 8)
        );
    }
}
