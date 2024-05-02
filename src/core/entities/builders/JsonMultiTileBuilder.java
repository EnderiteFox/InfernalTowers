package core.entities.builders;

import api.Position;
import api.world.World;
import core.entities.MultiTile;
import core.entities.instances.multitiles.Tower;
import core.utils.builders.JsonBuilder;

import java.util.Map;
import java.util.Optional;

public class JsonMultiTileBuilder extends JsonBuilder<MultiTile> {
    public JsonMultiTileBuilder(World world) {
        this(world, false);
    }

    public JsonMultiTileBuilder(World world, boolean debugMode) {
        super(debugMode);
        builderMap.put(
            "tower",
            json -> {
                Position pos = json.<Map<String, Object>>getObjectAtPath("position").map(
                        map -> json.parsePosition(map, world)
                            .orElseThrow(
                                () -> new IllegalArgumentException(
                                    "Failed to parse position from " + json.displayJson(map)
                                )
                            )
                    )
                    .orElseThrow(
                        () -> new IllegalArgumentException("Missing json key: position")
                    );
                Optional<Number> size = json.getObjectAtPath("size");
                return size.map(number -> new Tower(pos, number.intValue())).orElseGet(() -> new Tower(pos));
            }
        );
    }
}
