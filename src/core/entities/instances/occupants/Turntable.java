package core.entities.instances.occupants;

import api.Direction;
import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.GuiDisplayable;
import api.entities.entitycapabilities.Redirector;
import com.almasb.fxgl.entity.Entity;
import core.ImplDirection;
import core.entities.Moving;
import core.entities.Occupant;
import core.utils.DeferredAsset;
import core.utils.display.BlockDisplay;
import core.utils.display.CameraState;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Turntable extends Occupant implements Redirector, ConsoleDisplayable, GuiDisplayable {
    private final DeferredAsset<ImageView> view = new DeferredAsset<>(
        () -> BlockDisplay.buildImageView("/assets/occupants/turntable.png")
    );
    private final DeferredAsset<Entity> entity = new DeferredAsset<>(() -> BlockDisplay.buildEntity(view.get()));
    private static final double ROTATION_SPEED = 1;

    private int charAnimProgress = -1;

    public Turntable(Position position) {
        super(position);
    }

    @Override
    public void redirect(Moving m) {
        List<Direction> directions = new ArrayList<>();
        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                if (x == 0 && z == 0) continue;
                Direction dir = new ImplDirection(x, 0, z);
                Optional<Occupant> occupant = getPosition().add(dir).getOccupant();
                if (occupant.isEmpty() || occupant.get() instanceof Turntable) directions.add(dir);
            }
        }
        if (directions.isEmpty()) return;
        Direction redirectDir = directions.get((int) (Math.random() * directions.size()));
        Optional<Occupant> occupant = getPosition().add(redirectDir).getOccupant();
        if (occupant.isPresent() && occupant.get() instanceof Turntable turntable) {
            turntable.redirect(m);
            return;
        }
        m.setDirection(redirectDir);
        m.setPosition(getPosition().add(redirectDir));
    }

    @Override
    public char toChar() {
        final String charAnim = "|/-\\|/-\\";
        charAnimProgress++;
        charAnimProgress %= charAnim.length();
        return charAnim.charAt(charAnimProgress);
    }

    @Override
    public Entity getEntity() {
        return entity.get();
    }

    @Override
    public ImageView getView() {
        return view.get();
    }

    @Override
    public void updateNode(CameraState cameraState) {
        BlockDisplay.updateImageBlock(view.get(), entity.get(), getPosition(), cameraState);
        view.get().setRotate(view.get().getRotate() + ROTATION_SPEED);
    }
}
