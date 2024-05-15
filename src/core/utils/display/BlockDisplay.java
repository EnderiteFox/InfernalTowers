package core.utils.display;

import api.Position;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import core.gameinterface.GuiInterface;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BlockDisplay {
    private static final double BOTTOM_ALPHA = 0.6;
    private static final double TOP_ALPHA = 0.2;

    public static Entity buildEntity(Node view) {
        return FXGL.entityBuilder().view(view).buildAndAttach();
    }

    public static ImageView buildImageView(String imageName) {
        ImageView view = new ImageView(
            new Image(imageName,
                FXGL.getAppWidth(), FXGL.getAppHeight(),
                false, false)
        );
        view.setFitWidth(GuiInterface.TILE_SIZE);
        view.setFitHeight(GuiInterface.TILE_SIZE);
        return view;
    }

    public static void updateImageBlock(
        ImageView view, Entity entity, Position blockPos,
        CameraState camera
    ) {
        view.setFitHeight(GuiInterface.TILE_SIZE * camera.zoom());
        view.setFitWidth(GuiInterface.TILE_SIZE * camera.zoom());
        double[] screenPos = GuiInterface.getScreenSpacePos(blockPos, camera.zoom(), camera.camX(), camera.camZ());
        entity.setPosition(screenPos[0], screenPos[1]);
        entity.setOpacity(
            blockPos.getY() < camera.camY() ? BOTTOM_ALPHA : blockPos.getY() > camera.camY() ? TOP_ALPHA : 1
        );
    }
}
