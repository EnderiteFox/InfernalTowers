package core.entities.instances.multitileparts.ladder;

import api.Direction;
import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.Redirector;
import core.ImplDirection;
import core.entities.Moving;
import core.entities.MultiTilePart;
import core.entities.instances.multitiles.Ladder;

import java.util.ArrayList;
import java.util.List;

public class LadderBottom extends MultiTilePart<Ladder> implements Redirector, ConsoleDisplayable {
    public LadderBottom(Position position, Ladder multiTile) {
        super(position, multiTile);
    }

    protected Direction getRedirectDirection() {
        return new ImplDirection(0, 1, 0);
    }

    @Override
    public void redirect(Moving m) {
        Direction redirectDirection = getRedirectDirection();
        if (
            (redirectDirection.getY() < 0 && m.getDirection().getY() > 0)
                || (redirectDirection.getY() > 0 && m.getDirection().getY() < 0)
        ) {
            List<Direction> exitDirections = new ArrayList<>();
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    Direction dir = new ImplDirection(x, 0, z);
                    if (getPosition().clone().add(dir).getOccupant().isPresent()) continue;
                    if (
                        getPosition().getY() != 0
                            && getPosition().clone().add(dir).add(0, -1, 0).getOccupant().isEmpty()
                    ) continue;
                    exitDirections.add(dir);
                }
            }
            if (exitDirections.isEmpty()) m.getDirection().multiply(-1);
            else {
                Direction exitDir = exitDirections.get((int) (Math.random() * exitDirections.size()));
                m.setPosition(getPosition().clone().add(exitDir));
                m.setDirection(exitDir);
            }
        }
        else {
            if (getPosition().clone().add(redirectDirection).getOccupant().isPresent()) m.getDirection().multiply(-1);
            else {
                m.setPosition(getPosition().clone().add(redirectDirection));
                m.setDirection(redirectDirection);
            }
        }
    }

    @Override
    public char toChar() {
        return 'L';
    }
}
