package core.world.loaders;

import api.world.World;
import api.world.WorldLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileWorldLoader implements WorldLoader {
    private static final Map<String, WorldLoader> fileLoaders = new HashMap<>();

    public FileWorldLoader(boolean debugMode) {
        fileLoaders.put("txt", new TxtWorldLoader());
        fileLoaders.put("json", new JsonWorldLoader(debugMode));
    }

    @Override
    public World loadWorld(String filePath) throws IOException {
        String[] split = filePath.split("\\.");
        String extension = split[split.length - 1];
        if (!fileLoaders.containsKey(extension)) {
            System.out.println("Unsupported extension: " + extension);
            return null;
        }
        WorldLoader loader = fileLoaders.get(extension);
        try {
            return loader.loadWorld(filePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
