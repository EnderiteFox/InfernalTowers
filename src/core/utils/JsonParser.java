package core.utils;

import api.Direction;
import api.Position;
import api.world.World;
import core.ImplDirection;
import core.ImplPosition;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class JsonParser {
    private final Map<String, Object> json;

    public JsonParser(Map<String, Object> json) {
        this.json = json;
    }

    public JsonParser(String filePath) throws IOException {
        this.json = parseJson(filePath);
    }

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
                return Float.parseFloat(json);
            }
        }
    }

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

    private String[] getKeyValue(String str) {
        List<String> strings = separateArguments(':', str);
        return new String[] {strings.get(0).replaceAll("(^\")|(\"$)", ""), strings.get(1)};
    }

    public String displayJson(Object jsonObject) {
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

    public Optional<Direction> parseDirection(Map<String, Object> json) {
        if (!json.containsKey("x") || !json.containsKey("y") || !json.containsKey("z")) return Optional.empty();
        if (
            !(json.get("x") instanceof Number x)
                || !(json.get("y") instanceof Number y)
                || !(json.get("z") instanceof Number z)
        ) return Optional.empty();
        return Optional.of(new ImplDirection(x.intValue(), y.intValue(), z.intValue()));
    }

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

    public <T> Optional<T> getObjectAtPath(String path) {
        return getObjectAtPath(this.json, path);
    }

    public Optional<Position> parsePosition(Map<String, Object> json, World world) {
        Optional<Direction> optDir = parseDirection(json);
        if (optDir.isEmpty()) return Optional.empty();
        Direction dir = optDir.get();
        return Optional.of(new ImplPosition(world, dir.getX(), dir.getY(), dir.getZ()));
    }

    public Map<String, Object> getJson() {
        return json;
    }

    @Override
    public String toString() {
        return displayJson(this.json);
    }
}
