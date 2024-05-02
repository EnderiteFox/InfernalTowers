import api.InfernalTowerGame;
import api.world.World;
import api.world.WorldLoader;
import core.ImplInfernalTowerGame;
import core.gameinterface.ConsoleInterface;
import core.world.loaders.JsonWorldLoader;
import core.world.loaders.TxtWorldLoader;

import java.io.IOException;
import java.util.*;

/**
 * The main class
 */
public class Main {
    private static final List<String> MANDATORY_ARGUMENTS = List.of(
        "w"
    );
    private static final List<String> OPTIONAL_ARGUMENTS = List.of(
        "t",
        "debug"
    );
    private static final List<String> SOLO_ARGUMENTS = List.of(
        "help"
    );
    private static final Map<String, WorldLoader> fileLoaders = new HashMap<>();

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


        fileLoaders.put("txt", new TxtWorldLoader());
        fileLoaders.put("json", new JsonWorldLoader(arguments.containsKey("debug")));


        float frameRate = 1;
        if (arguments.containsKey("t")) {
            try {
                frameRate = Float.parseFloat(arguments.get("t"));
            } catch (NumberFormatException e) {
                System.out.println("Invalid value for parameter t: " + arguments.get("t") + ", defaulting to 1");
            }
        }
        Optional<World> optWorld = loadWorld(arguments.get("w"));
        if (optWorld.isEmpty()) return;
        World world = optWorld.get();
        InfernalTowerGame game = new ImplInfernalTowerGame(
            new ConsoleInterface(world), world, frameRate
        );
        game.startGame();
    }

    private static Optional<World> loadWorld(String filePath) {
        String[] split = filePath.split("\\.");
        String extension = split[split.length - 1];
        if (!fileLoaders.containsKey(extension)) {
            System.out.println("Unsupported extension: " + extension);
            return Optional.empty();
        }
        WorldLoader loader = fileLoaders.get(extension);
        try {
            return Optional.of(loader.loadWorld(filePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
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
            "-t: Choose the tick rate of the game (in tick per seconds)"
        ).forEach(System.out::println);
    }
}
