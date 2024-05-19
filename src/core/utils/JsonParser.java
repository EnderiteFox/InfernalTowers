package core.utils;

import api.Direction;
import api.Position;
import api.world.World;
import core.ImplDirection;
import core.ImplPosition;

import java.io.*;
import java.util.*;

/**
 * <p>A class that can parse and encapsulate JSON files
 * <p>
 * <p>The json data can be:<ul>
 *     <li>A {@code Map<String, Object>} for {@code {key: value}}</li>
 *     <li>A {@code List<Object>} for {@code [value1, value2]}</li>
 *     <li>A {@code String} for {@code "value"}</li>
 *     <li>A {@code Boolean} for {@code true} or {@code false}</li>
 *     <li>A {@code Number} for {@code 1} or {@code 1.0}</li>
 * </ul>
 */
public class JsonParser {
    private final Map<String, Object> json;

    /**
     * Create a JsonParser from an already parsed file
     * @param json The json data
     */
    public JsonParser(Map<String, Object> json) {
        this.json = json;
    }

    /**
     * Creates a JsonParser by parsing a JSON file
     * @param filePath The path of the file to parse
     * @throws IOException If an IOException occurs while reading the file
     */
    public JsonParser(String filePath) throws IOException {
        this.json = parseJson(filePath);
    }

    /**
     * Reads the given file and parses it
     * @param filePath The path of the file to read
     * @return The data of the JSON file as a {@code Map<String, Object>}
     * @throws IOException If an IOException occurs while reading the file
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJson(String filePath) throws IOException {
        StringBuilder str = new StringBuilder();
        File file = new File(filePath);
        if (!file.exists()) throw new FileNotFoundException("Unable to find JSON file at " + filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) str.append(line).append('\n');
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException("Unable to find JSON file at " + filePath);
        }
        String json = str.toString();
        json = json.replaceAll("([{},:\\[\\]])[\n ]+", "$1");
        json = json.replaceAll("[\n ]+([{},:\\[\\]])", "$1");
        Object obj = parseJsonString(json);
        if (!(obj instanceof Map<?, ?> map)) throw new IOException("Malformed JSON file");
        return (Map<String, Object>) map;
    }

    /**
     * Parses the given string and returns a json object
     * @param json The json data to parse
     * @return The parsed json object
     * @throws IOException If the string is malformed
     */
    private Object parseJsonString(String json) throws IOException {
        if (json.isEmpty()) throw new IOException("Malformed JSON file");
        switch (json.charAt(0)) {
            case '{' -> {
                json = json.replaceAll("^\\{", "");
                json = json.replaceAll("}$", "");
                Map<String, Object> map = new LinkedHashMap<>();
                for (String arg : separateArguments(',', json)) {
                    String[] values = getKeyValue(arg);
                    map.put(values[0], parseJsonString(values[1]));
                }
                return map;
            }
            case '[' -> {
                json = json.replaceAll("^\\[", "");
                json = json.replaceAll("]$", "");
                List<Object> list = new ArrayList<>();
                for (String arg : separateArguments(',', json)) list.add(parseJsonString(arg));
                return list;
            }
            case '"' -> {
                json = json.replaceAll("^\"", "");
                json = json.replaceAll("\"$", "");
                return json;
            }
            default -> {
                if (json.equals("true")) return true;
                if (json.equals("false")) return false;
                return Float.parseFloat(json);
            }
        }
    }

    /**
     * Separates the elements of the given json string, ignoring internal json elements like arrays or strings
     * @param separator The separator to use
     * @param json The json string
     * @return The list of elements
     */
    private List<String> separateArguments(char separator, String json) {
        List<String> arguments = new ArrayList<>();
        boolean isInString = false;
        int inArray = 0;
        StringBuilder str = new StringBuilder();
        for (char chr : json.toCharArray()) {
            if (chr == '"') isInString = !isInString;
            if (chr == '[' || chr == '{') inArray++;
            if (chr == ']' || chr == '}') inArray--;
            if (chr == separator && !isInString && inArray == 0) {
                arguments.add(str.toString());
                str = new StringBuilder();
            }
            else str.append(chr);
        }
        arguments.add(str.toString());
        return arguments;
    }

    /**
     * Parses the keys from the given json string
     * @param str The json string
     * @return The list of keys
     */
    private String[] getKeyValue(String str) {
        List<String> strings = separateArguments(':', str);
        return new String[] {strings.get(0).replaceAll("(^\")|(\"$)", ""), strings.get(1)};
    }

    /**
     * Displays a json object properly
     * @param jsonObject The json object to display
     * @return The string representation of the json object
     */
    public String displayJson(Object jsonObject) {
        if (jsonObject instanceof Boolean bool) return bool ? "true" : "false";
        if (jsonObject instanceof String str) return "\"" + str + "\"";
        if (jsonObject instanceof Float flt) return String.valueOf(flt);
        if (jsonObject instanceof List<?> list) {
            StringBuilder str = new StringBuilder("[");
            for (int i = 0; i < list.size(); ++i) {
                str.append(displayJson(list.get(i)));
                if (i != list.size() - 1) str.append(", ");
            }
            str.append(']');
            return str.toString();
        }
        if (jsonObject instanceof Map<?, ?> map) {
            StringBuilder str = new StringBuilder("{");
            for (int i = 0; i < map.keySet().size(); ++i) {
                str.append("\"").append(map.keySet().toArray()[i]).append("\": ");
                str.append(displayJson(map.values().toArray()[i]));
                if (i != map.keySet().size() - 1) str.append(", ");
            }
            str.append('}');
            return str.toString();
        }
        return jsonObject.toString();
    }

    /**
     * Parses a map to get a Direction from it. Ignored coordinates defaults to 0
     * @param json The map to parse
     * @return An Optional of the Direction, or an empty Optional if the direction failed to be parsed
     */
    public Optional<Direction> parseDirection(Map<String, Object> json) {
        // Failsafe to know if the given map is not supposed to be a position, by forcing at least one coordinate
        if (!json.containsKey("x") && !json.containsKey("y") && !json.containsKey("z")) return Optional.empty();
        int x = this.<Number>getObjectAtPath(json, "x").orElse(0).intValue();
        int y = this.<Number>getObjectAtPath(json, "y").orElse(0).intValue();
        int z = this.<Number>getObjectAtPath(json, "z").orElse(0).intValue();
        return Optional.of(new ImplDirection(x, y, z));
    }

    /**
     * Returns the object at the given path in the json data
     * @param json The json data
     * @param path The path to get the data from
     * @return An optional of the data if present, or an empty optional
     * @param <T> The type of the data to get
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getObjectAtPath(Map<String, Object> json, String path) {
        String[] keys = path.split("\\.");
        if (keys.length == 0) return Optional.empty();
        for (int i = 1; i < keys.length - 1; i++) {
            if (!json.containsKey(keys[i])) return Optional.empty();
            else {
                if (!(json.get(keys[i]) instanceof Map<?, ?> map)) return Optional.empty();
                json = (Map<String, Object>) map;
            }
        }
        if (json == null) return Optional.empty();
        if (!json.containsKey(keys[keys.length - 1])) return Optional.empty();
        T result;
        try {
            result = (T) json.get(keys[keys.length - 1]);
        } catch (Exception ignored) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    /**
     * An overload of {@link JsonParser#getObjectAtPath(Map, String)} that uses the json data stored in this JsonParser
     * @param path The path to get the data from
     * @return An optional of the data if present, or an empty optional
     * @param <T> The type of the data to get
     */
    public <T> Optional<T> getObjectAtPath(String path) {
        return getObjectAtPath(this.json, path);
    }

    /**
     * Parses a {@link Position} out of a json object
     * @param json The json object to parse
     * @param world The world used to create the position
     * @return An optional of the Position if built successfully, or an empty optional otherwise
     */
    public Optional<Position> parsePosition(Map<String, Object> json, World world) {
        Optional<Direction> optDir = parseDirection(json);
        if (optDir.isEmpty()) return Optional.empty();
        Direction dir = optDir.get();
        return Optional.of(new ImplPosition(world, dir.getX(), dir.getY(), dir.getZ()));
    }

    /**
     * @return the raw Json data stored in this parser
     */
    public Map<String, Object> getJson() {
        return json;
    }

    @Override
    public String toString() {
        return displayJson(this.json);
    }
}
