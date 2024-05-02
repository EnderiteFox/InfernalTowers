package core.entities.builders;

import api.Position;
import api.world.World;
import core.entities.Occupant;
import core.entities.instances.tiles.Border;
import core.entities.instances.tiles.Human;
import core.utils.builders.JsonBuilder;

import java.util.Map;
import java.util.function.Function;

public class JsonEntityBuilder extends JsonBuilder<Occupant> {
    public JsonEntityBuilder(World world) {
        this(world, false);
    }

    public JsonEntityBuilder(World world, boolean debugMode) {
        super(debugMode);
        registerFromPos("human", Human::new, world);
        registerFromPos("border", Border::new, world);
    }

    public void registerFromPos(String type, Function<Position, Occupant> func, World world) {
        builderMap.put(
            type,
            (json) -> {
                Position pos = json.<Map<String, Object>>getObjectAtPath("position")
                    .map(
                        map -> json.parsePosition(map, world)
                            .orElseThrow(
                                () -> new IllegalArgumentException(
                                    "Failed to parse position from " + json.displayJson(map)
                                )
                            )
                    ).orElseThrow(
                        () -> new IllegalArgumentException("Missing json key: position")
                    );
                return func.apply(pos);
            }
        );
    }
}
