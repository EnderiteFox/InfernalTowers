package api;

public interface Moveable {
    Position getPresentPosition();
    Position getTargetPosition();
    boolean moveTo(Position pos);
}
