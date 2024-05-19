package core.entities.builders;

import api.Direction;
import api.Position;
import api.world.World;
import core.entities.Occupant;
import core.entities.instances.occupants.Border;
import core.entities.instances.occupants.Human;
import core.entities.instances.occupants.QuantumBox;
import core.entities.instances.occupants.RotatingPanel;
import core.utils.JsonParser;
import core.utils.builders.JsonBuilder;
import core.world.ImplWorld;
import core.world.loaders.FileWorldLoader;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A {@link JsonBuilder} that can build {@link Occupant}s
 */
public class JsonEntityBuilder extends JsonBuilder<Occupant> {
    private final World world;

    public JsonEntityBuilder(World world, boolean debugMode) {
        super(debugMode);
        this.world = world;
        builderMap.put("human", this::buildHuman);
        registerFromPos("border", Border::new);
        builderMap.put("quantumBox", this::buildRelativityBox);
        builderMap.put("rotatingPanel", this::buildRotatingPanel);
    }

    public void registerFromPos(String type, Function<Position, Occupant> func) {
        builderMap.put(
            type,
            json -> func.apply(requirePosition(json, "position", world))
        );
    }

    private Occupant buildHuman(JsonParser json) {
        Position pos = requirePosition(json, "position", world);
        Optional<Direction> dir = json.<Map<String, Object>>getObjectAtPath("direction").flatMap(json::parseDirection);
        Optional<UUID> uuid = json.<String>getObjectAtPath("uuid").map(UUID::fromString);
        Human human = uuid.map(uid -> new Human(pos, uid)).orElse(new Human(pos));
        dir.ifPresent(human::setDirection);
        return human;
    }

    private Occupant buildRelativityBox(JsonParser json) {
        Position pos = requirePosition(json, "position", world);
        World boxWorld;
        boxWorld = json.<String>getObjectAtPath("world")
            .map(s -> {
                try {
                    return new FileWorldLoader(debugMode).loadWorld(s, world.getEventManager());
                } catch (IOException e) {
                    if (debugMode) {
                        System.out.println("Failed to load RelativityBox world at " + pos
                            + ", defaulting to an empty world");
                        System.out.println(e.getMessage());
                    }
                }
                return new ImplWorld(world.getEventManager());
            })
            .orElse(new ImplWorld(world.getEventManager()));
        Optional<Integer> size = json.<Number>getObjectAtPath("size").map(Number::intValue);
        if (size.isPresent()) return new QuantumBox(pos, boxWorld, size.get());
        Supplier<IllegalArgumentException> noSizeError = () -> new IllegalArgumentException(
            "Missing Json key: either size or both width and height"
        );
        Integer width = json.<Number>getObjectAtPath("width").map(Number::intValue).orElseThrow(noSizeError);
        Integer height = json.<Number>getObjectAtPath("height").map(Number::intValue).orElseThrow(noSizeError);
        return new QuantumBox(pos, boxWorld, width, height);
    }

    private Occupant buildRotatingPanel(JsonParser json) {
        Position pos = requirePosition(json, "position", world);
        Boolean isRotated = json.<Boolean>getObjectAtPath("rotated").orElse(false);
        return new RotatingPanel(pos, isRotated);
    }
}
