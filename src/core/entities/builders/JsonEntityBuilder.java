package core.entities.builders;

import api.Direction;
import api.Position;
import api.world.World;
import core.entities.Occupant;
import core.entities.instances.occupants.Border;
import core.entities.instances.occupants.Human;
import core.entities.instances.occupants.RelativityBox;
import core.utils.JsonParser;
import core.utils.builders.JsonBuilder;
import core.world.ImplWorld;
import core.world.loaders.FileWorldLoader;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class JsonEntityBuilder extends JsonBuilder<Occupant> {
    private final World world;
    public JsonEntityBuilder(World world) {
        this(world, false);
    }

    public JsonEntityBuilder(World world, boolean debugMode) {
        super(debugMode);
        this.world = world;
        builderMap.put("human", getHumanBuilder());
        registerFromPos("border", Border::new);
        builderMap.put("relativityBox", getRelativityBoxBuilder());
    }

    public void registerFromPos(String type, Function<Position, Occupant> func) {
        builderMap.put(
            type,
            json -> func.apply(requirePosition(json, "position", world))
        );
    }

    private Function<JsonParser, Occupant> getHumanBuilder() {
        return json -> {
            Position pos = requirePosition(json, "position", world);
            Optional<Direction> dir = json.<Map<String, Object>>getObjectAtPath("direction").flatMap(json::parseDirection);
            return dir.map(d -> new Human(pos, d)).orElse(new Human(pos));
        };
    }

    private Function<JsonParser, Occupant> getRelativityBoxBuilder() {
        return json -> {
            Position pos = requirePosition(json, "position", world);
            World boxWorld;
            boxWorld = json.<String>getObjectAtPath("world")
                .map(s -> {
                    try {
                        return new FileWorldLoader(debugMode).loadWorld(s);
                    } catch (IOException e) {
                        if (debugMode) {
                            System.out.println("Failed to load RelativityBox world at " + pos
                                + ", defaulting to an empty world");
                            System.out.println(e.getMessage());
                        }
                    }
                    return new ImplWorld();
                })
                .orElse(new ImplWorld());
            Optional<Integer> size = json.<Number>getObjectAtPath("size").map(Number::intValue);
            if (size.isPresent()) return new RelativityBox(pos, boxWorld, size.get());
            Supplier<IllegalArgumentException> noSizeError = () -> new IllegalArgumentException(
                "Missing Json key: either size or both width and height"
            );
            Integer width = json.<Number>getObjectAtPath("width").map(Number::intValue).orElseThrow(noSizeError);
            Integer height = json.<Number>getObjectAtPath("height").map(Number::intValue).orElseThrow(noSizeError);
            return new RelativityBox(pos, boxWorld, width, height);
        };
    }
}
