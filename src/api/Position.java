package api;

public interface Position {
    int getX();
    void setX(int x);
    int getY();
    void setY(int y);
    int getZ();
    void setZ(int z);
    Position add(int x, int y, int z);
    Position add(Position other);
    Position multiply(int x, int y, int z);
    Position multiply(int m);
    Position copy();
    Position normalize();
}
