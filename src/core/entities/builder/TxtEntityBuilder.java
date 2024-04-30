package core.entities.builder;

import core.entities.Occupant;
import core.entities.instances.Border;
import core.entities.instances.Human;

public class TxtEntityBuilder extends TxtBuilder<Occupant> {

    public TxtEntityBuilder() {
        registerFromString("H", (chr, pos) -> new Human(pos));
        registerFromString("#", (chr, pos) -> new Border(pos));
    }
}
