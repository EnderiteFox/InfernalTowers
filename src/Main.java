import api.EventManager;
import api.InfernalTowerGame;
import api.gameinterface.GameInterface;
import api.world.World;
import core.events.ImplEventManager;
import core.game.ConsoleInfernalTowerGame;
import core.game.GuiInfernalTowerGame;
import core.gameinterface.ConsoleInterface;
import core.gameinterface.GuiInterface;
import core.world.loaders.FileWorldLoader;

import java.io.IOException;
import java.util.*;

/**
 * <p>The main class, that parses jar arguments and starts the game
 * <p>This project uses FXGL as a graphical interface. See <a href="https://almasb.github.io/FXGL/">this</a>
 */
public class Main {
    private static final List<String> MANDATORY_ARGUMENTS = List.of(
        "w"
    );
    private static final List<String> OPTIONAL_ARGUMENTS = List.of(
        "t",
        "debug",
        "g"
    );
    private static final List<String> SOLO_ARGUMENTS = List.of(
        "help"
    );

    /**
     * Reads command line arguments, and starts the game
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Map<String, String> arguments;
        try {
            arguments = parseParameters(
                args,
                MANDATORY_ARGUMENTS,
                OPTIONAL_ARGUMENTS,
                SOLO_ARGUMENTS
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Use the --help option to get help");
            return;
        }

        if (arguments.containsKey("help")) {
            printHelp();
            return;
        }

        float frameRate = 1;
        if (arguments.containsKey("t")) {
            try {
                frameRate = Float.parseFloat(arguments.get("t"));
            } catch (NumberFormatException e) {
                System.out.println("Invalid value for parameter t: " + arguments.get("t") + ", defaulting to 1");
            }
        }

        EventManager eventManager = new ImplEventManager(arguments.containsKey("debug"));
        World world;
        try {
            world = new FileWorldLoader(arguments.containsKey("debug")).loadWorld(arguments.get("w"), eventManager);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (world == null) return;
        GameInterface gameInterface = getGameInterface(arguments.getOrDefault("g", "console"), world);
        if (gameInterface == null) return;
        InfernalTowerGame game = getGameInstance(
            arguments.getOrDefault("g", "console"), gameInterface, world, frameRate
        );
        if (game == null) return;
        game.startGame();
    }

    /**
     * <p>Parses the list of arguments and returns a map containing each argument and their value
     * <p>Also controls the validity of the arguments
     * @param args The list of arguments to parse
     * @param mandatory A list of mandatory arguments
     * @param optional A list of optional arguments
     * @param soloOptions A list of solo options. When one of these options are detected, the mandatory argument conditions are ignored
     * @return A map containing each argument as a key, and their value for keyed arguments
     */
    public static Map<String, String> parseParameters(
        String[] args,
        List<String> mandatory,
        List<String> optional,
        List<String> soloOptions
    ) {
        Stack<Character> stack = new Stack<>();
        Map<String, String> map = new HashMap<>();
        for (String arg : args) {
            if (arg.matches("^--[^-].*")) map.put(arg.replaceFirst("^--", ""), "");
            else if (arg.matches("^-[^-]*")) {
                Arrays.stream(
                    arg.replaceFirst("^-", "")
                        .split("")
                ).forEach(
                    s -> stack.push(s.charAt(0))
                );
            }
            else {
                if (stack.isEmpty()) throw new IllegalArgumentException("Invalid argument: " + arg);
                map.put(String.valueOf(stack.pop()), arg);
            }
        }
        if (!stack.isEmpty()) throw new IllegalArgumentException("Missing value for key " + stack.pop());
        if (soloOptions.stream().noneMatch(map::containsKey)) {
            for (String str : mandatory) {
                if (!map.containsKey(str)) throw new IllegalArgumentException("Missing mandatory argument: " + str);
            }
        }
        for (String key : map.keySet()) if (
            !mandatory.contains(key) && !optional.contains(key) && !soloOptions.contains(key)
        ) {
            throw new IllegalArgumentException("Invalid argument: " + key);
        }
        return map;
    }

    /**
     * Returns a {@link GameInterface} from its name
     * @param interfaceName The name of the interface
     * @param world The world to use in the interface
     * @return The corresponding interface or {@code null} if no interface corresponds to the given name
     */
    public static GameInterface getGameInterface(String interfaceName, World world) {
        switch (interfaceName) {
            case "console":
                return new ConsoleInterface(world);
            case "gui":
                return new GuiInterface(world);
            default:
                System.out.println("Invalid game interface: " + interfaceName);
                return null;
        }
    }

    /**
     * Returns a {@link InfernalTowerGame} from its name
     * @param instanceName The name of the game instance to use
     * @param gameInterface The game interface to use
     * @param world The world to use for the game
     * @param frameRate The frame rate to run the game at
     * @return The corresponding game instance, or {@code null} if no game instance corresponds to the given name
     */
    public static InfernalTowerGame getGameInstance(
        String instanceName,
        GameInterface gameInterface,
        World world,
        float frameRate
    ) {
        switch (instanceName) {
            case "console":
                return new ConsoleInfernalTowerGame(gameInterface, world, frameRate);
            case "gui":
                return new GuiInfernalTowerGame(gameInterface, world, frameRate);
            default:
                System.out.println("Invalid game interface: " + instanceName);
                return null;
        }
    }

    /**
     * Prints the help message to use when using the --help jar option
     */
    public static void printHelp() {
        List.of(
            "Use '--option' to add the boolean argument named 'option'",
            "Use '-key value' to add the value named 'value' to the key 'key'",
            "",
            "Boolean options:",
            "--help: Display this help message",
            "--debug: Display debug messages",
            "",
            "Keyed options:",
            "-w (mandatory): Choose the world file to load",
            "-t: Choose the tick rate of the game (in tick per seconds)",
            "-g: Choose the game interface to use. Can be either console or gui. If not present, will default to console"
        ).forEach(System.out::println);
    }
}
