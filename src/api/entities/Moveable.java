package api.entities;

import api.Position;

public interface Moveable {
    Position getPresentPosition();
    Position getTargetPosition();
    boolean moveTo(Position pos);
}
