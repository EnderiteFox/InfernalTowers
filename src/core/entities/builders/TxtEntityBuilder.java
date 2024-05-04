package core.entities.builders;

import core.entities.Occupant;
import core.entities.instances.occupants.Border;
import core.entities.instances.occupants.Human;
import core.utils.builders.TxtBuilder;

public class TxtEntityBuilder extends TxtBuilder<Occupant> {

    public TxtEntityBuilder() {
        registerFromString("H", (chr, pos) -> new Human(pos));
        registerFromString("#", (chr, pos) -> new Border(pos));
    }
}
