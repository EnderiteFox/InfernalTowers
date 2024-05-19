package core.utils.display;

import api.Direction;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import core.gameinterface.GuiInterface;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A class containing functions that can be used to display blocks on a grid using an FXGL graphical interface
 */
public class BlockDisplay {
    private static final double BOTTOM_ALPHA = 0.6;
    private static final double TOP_ALPHA = 0.2;

    /**
     * Builds an FXGL entity using the given view
     * @param view The view to use for the entity
     * @return The built entity
     */
    public static Entity buildEntity(Node view) {
        Entity entity = FXGL.entityBuilder().view(view).buildAndAttach();
        entity.setVisible(false);
        return entity;
    }

    /**
     * An overload of {@link BlockDisplay#buildImageView(String, int, int)} for blocks that have a size of 1
     * @param imageName The path to the image
     * @return The built ImageView
     */
    public static ImageView buildImageView(String imageName) {
        return buildImageView(imageName, 1, 1);
    }

    /**
     * Builds an FXGL ImageView with the given image path and rescales the image to fit the given block size
     * @param imageName The path to the image
     * @param blockWidth The width of the block
     * @param blockHeight The height of the block
     * @return The built ImageView
     */
    public static ImageView buildImageView(String imageName, int blockWidth, int blockHeight) {
        ImageView view = new ImageView(
            new Image(
                imageName,
                FXGL.getAppWidth(), FXGL.getAppHeight(),
                false, false
            )
        );
        view.setFitWidth(GuiInterface.TILE_SIZE * blockWidth);
        view.setFitWidth(GuiInterface.TILE_SIZE * blockHeight);
        return view;
    }

    /**
     * An overload of {@link BlockDisplay#updateImageBlock(ImageView, Entity, Direction, CameraState, int, int)} for
     * blocks that have a size of 1
     * @param view The FXGL ImageView of the block
     * @param entity The FXGL Entity of the block
     * @param blockPos The position of the block
     * @param camera The current camera state
     */
    public static void updateImageBlock(ImageView view, Entity entity, Direction blockPos, CameraState camera) {
        updateImageBlock(view, entity, blockPos, camera, 1, 1);
    }

    /**
     * Updates a block
     * @param view The FXGL ImageView of the block
     * @param entity The FXGL Entity of the block
     * @param blockPos The position of the block
     * @param camera The current camera state
     * @param blockWidth The width of the block
     * @param blockHeight The height of the block
     */
    public static void updateImageBlock(
        ImageView view, Entity entity, Direction blockPos,
        CameraState camera, int blockWidth, int blockHeight
    ) {
        double zoom = camera.zoom() + (blockPos.getY() - camera.camY()) * (1.0 / (GuiInterface.TILE_SIZE));
        view.setFitHeight(GuiInterface.TILE_SIZE * zoom * blockHeight);
        view.setFitWidth(GuiInterface.TILE_SIZE * zoom * blockWidth);
        double[] screenPos = GuiInterface.getScreenSpacePos(blockPos, zoom, camera.camX(), camera.camZ());
        entity.setPosition(screenPos[0], screenPos[1]);
        entity.setZIndex(blockPos.getY());
        entity.setOpacity(
            blockPos.getY() < camera.camY() ? BOTTOM_ALPHA : blockPos.getY() > camera.camY() ? TOP_ALPHA : 1
        );
    }
}
