import api.InfernalTowerGame;
import api.world.World;
import api.world.WorldLoader;
import core.ImplInfernalTowerGame;
import core.gameinterface.ConsoleInterface;
import core.world.loaders.TxtWorldLoader;

import java.util.*;

/**
 * The main class
 */
public class Main {
    /**
     * Reads command line arguments, and starts the game
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Map<String, String> arguments;
        try {
            arguments = parseParameters(args, List.of("w"), List.of(), List.of("help"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Use the --help option to get help");
            return;
        }
        if (arguments.containsKey("help")) {
            printHelp();
            return;
        }
        WorldLoader worldLoader = new TxtWorldLoader();
        World world = worldLoader.loadWorld(arguments.get("w"));
        InfernalTowerGame game = new ImplInfernalTowerGame(
            new ConsoleInterface(world), world
        );
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

    public static void printHelp() {
        List.of(
            "Use '--option' to add the boolean argument named 'option'",
            "Use '-key value' to add the value named 'value' to the key 'key'",
            "",
            "Boolean options:",
            "--help: Display this help message",
            "",
            "Keyed options:",
            "-w (mandatory): Choose the world file to load"
        ).forEach(System.out::println);
    }
}
