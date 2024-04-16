package core.world;

import api.world.World;
import api.world.WorldLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ImplWorldLoader implements WorldLoader {
    @Override
    public World loadWorld(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return worldNotFoundWarning(filePath);
        World world = new ImplWorld();
        try (FileReader reader = new FileReader(file)) {
            readWorld(reader, world);
        }
        catch (FileNotFoundException e) {
            return worldNotFoundWarning(filePath);
        }
        catch (IOException e) {
            System.out.println("An IO exception occured while reading world");
            return new ImplWorld();
        }
        return world;
    }

    private World worldNotFoundWarning(String filePath) {
        System.out.println("Unable to find world at path " + filePath);
        return new ImplWorld();
    }

    private void readWorld(FileReader reader, World world) {
        
    }
}
