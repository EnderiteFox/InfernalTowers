package core.utils;

import api.utils.CharGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class that can display characters on a grid and display them correctly,
 * and automatically adapts to the required size
 */
public class ImplCharGrid implements CharGrid {
    private final List<String> tab = new ArrayList<>();
    private final int[] minCoords = {Integer.MAX_VALUE, Integer.MAX_VALUE};

    /**
     * Sets the character at the required coordinates to another one
     * @param x The x coordinate in the grid
     * @param y The y coordinate in the grid
     * @param chr The character to set
     */
    @Override
    public void setChar(int x, int y, char chr) {
        if (minCoords[1] == Integer.MAX_VALUE) {
            tab.add(" ");
            minCoords[1] = y;
            minCoords[0] = x;
        }
        else if (y < minCoords[1]) {
            int toAdd = minCoords[1] - y;
            for (int i = 0; i < toAdd; ++i) {
                tab.add(0, " ".repeat(tab.get(tab.size() - 1).length()));
            }
            minCoords[1] = y;
        }
        int relativeY = y - minCoords[1];
        for (int i = tab.size(); i < relativeY + 1; ++i) tab.add(" ".repeat(tab.get(0).length()));

        if (x < minCoords[0]) {
            int toAdd = minCoords[0] - x;
            for (int i = 0; i < tab.size(); ++i) {
                StringBuilder builder = new StringBuilder(tab.get(i));
                builder.insert(0, " ".repeat(toAdd));
                tab.set(i, builder.toString());
            }
            minCoords[0] = x;
        }
        int relativeX = x - minCoords[0];
        for (int i = 0; i < tab.size(); ++i) {
            StringBuilder builder = new StringBuilder(tab.get(i));
            builder.append(" ".repeat(Math.max(0, relativeX - builder.length()) + 1));
            tab.set(i, builder.toString());
        }

        StringBuilder builder = new StringBuilder(tab.get(relativeY));
        builder.setCharAt(relativeX, chr);
        tab.set(relativeY, builder.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        tab.forEach(
            s -> {
                StringBuilder sBuilder = new StringBuilder();
                for (char chr : s.toCharArray()) sBuilder.append(chr).append(' ');
                builder.append(sBuilder.toString()).append('\n');
            }
        );
        return builder.toString();
    }
}
