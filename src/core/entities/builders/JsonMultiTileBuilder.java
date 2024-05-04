package core.entities.builders;

import api.Position;
import api.world.World;
import core.entities.MultiTile;
import core.entities.instances.multitiles.Ladder;
import core.entities.instances.multitiles.Tower;
import core.utils.JsonParser;
import core.utils.builders.JsonBuilder;

import java.util.Optional;
import java.util.function.Function;

public class JsonMultiTileBuilder extends JsonBuilder<MultiTile> {
    public JsonMultiTileBuilder(World world) {
        this(world, false);
    }

    public JsonMultiTileBuilder(World world, boolean debugMode) {
        super(debugMode);
        builderMap.put("tower", getTowerBuilder(world));
        builderMap.put("ladder", getLadderBuilder(world));
    }

    private Function<JsonParser, MultiTile> getTowerBuilder(World world) {
        return json -> {
            Position pos = requirePosition(json, "position", world);
            Optional<Number> size = json.getObjectAtPath("size");
            return size.map(number -> new Tower(pos, number.intValue())).orElseGet(() -> new Tower(pos));
        };
    }

    private Function<JsonParser, MultiTile> getLadderBuilder(World world) {
        return json -> {
            Position pos = requirePosition(json, "position", world);
            int size = this.<Number>requireKey(json, "size").intValue();
            return new Ladder(pos, size);
        };
    }
}
