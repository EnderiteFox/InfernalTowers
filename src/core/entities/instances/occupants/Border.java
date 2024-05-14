package core.entities.instances.occupants;

import api.Position;
import api.entities.entitycapabilities.ConsoleDisplayable;
import api.entities.entitycapabilities.GuiDisplayable;
import api.entities.entitycapabilities.Redirector;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import core.entities.Moving;
import core.entities.Occupant;
import core.gameinterface.GuiInterface;
import core.utils.DeferredAsset;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;

/**
 * A Border entity, making other entities bounce back
 */
public class Border extends Occupant implements Redirector, ConsoleDisplayable, GuiDisplayable {
    private final DeferredAsset<Entity> entity = new DeferredAsset<>(this::buildEntity);
    private final DeferredAsset<ImageView> view = new DeferredAsset<>(this::buildImageView);

    public Border(Position position) {
        super(position);
    }

    @Override
    public void redirect(Moving m) {
        Position pos = m.getPosition();
        if (m.getDirection().getY() != 0) m.getDirection().setY(0);
        if (m.getDirection().getX() == 0 || m.getDirection().getZ() == 0) m.getDirection().multiply(-1);
        else {
             Position xPos = pos.clone().add(m.getDirection().getX(), 0, 0);
             Position zPos = pos.clone().add(0, 0, m.getDirection().getZ());
             if (xPos.getOccupant().isEmpty() && zPos.getOccupant().isPresent()) {
                 pos = xPos;
                 m.getDirection().multiply(1, 1, -1);
             }
             else if (xPos.getOccupant().isPresent() && zPos.getOccupant().isEmpty()) {
                 pos = zPos;
                 m.getDirection().multiply(-1, 1, 1);
             }
             else m.getDirection().multiply(-1);
        }
        m.setPosition(pos);
    }

    @Override
    public char toChar() {
        return '#';
    }

    @Override
    public Entity getEntity() {
        return entity.get();
    }

    private Entity buildEntity() {
        return FXGL.entityBuilder()
            .view(view.get())
            .buildAndAttach();
    }

    private ImageView buildImageView() {
        ImageView view = new ImageView(new Image("/assets/occupants/brick.png", FXGL.getAppWidth(), FXGL.getAppHeight(), false, false));
        view.setFitWidth(GuiInterface.TILE_SIZE);
        view.setFitHeight(GuiInterface.TILE_SIZE);
        return view;
    }

    @Override
    public void updateNode(double zoom, double camX, double camZ) {
        view.get().setFitHeight(GuiInterface.TILE_SIZE * zoom);
        view.get().setFitWidth(GuiInterface.TILE_SIZE * zoom);
        double[] screenPos = GuiInterface.getScreenSpacePos(getPosition(), zoom, camX, camZ);
        entity.get().setPosition(screenPos[0], screenPos[1]);
    }
}
