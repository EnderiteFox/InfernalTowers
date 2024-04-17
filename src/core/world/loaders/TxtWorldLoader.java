package core.world.loaders;

import api.Position;
import api.entities.builder.EntityBuilder;
import api.world.World;
import api.world.WorldLoader;
import core.ImplPosition;
import core.entities.builder.TxtEntityBuilder;
import core.world.ImplWorld;

import java.io.*;

public class TxtWorldLoader implements WorldLoader {
    @Override
    public World loadWorld(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return worldNotFoundWarning(filePath);
        World world = new ImplWorld();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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

    private void readWorld(BufferedReader reader, World world) throws IOException {
        EntityBuilder<Character> entityBuilder = new TxtEntityBuilder();
        Position pos = new ImplPosition(world, 0, 0, 0);
        String line;
        while ((line = reader.readLine()) != null) {
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i += 2) {
                entityBuilder.build(chars[i], pos.clone()).ifPresent(o -> world.setOccupant(pos.clone(), o));
                pos.add(1, 0, 0);
            }
            pos.setX(0);
            pos.add(0, 0, 1);
        }
    }
}
