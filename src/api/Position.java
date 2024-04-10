package api;

public interface Position {
    int getX();
    int getY();
    int getZ();
    Position add(int x, int y, int z);
    Position add(Position other);
    Position clone();
}
