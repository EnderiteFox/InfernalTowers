package core.utils;

import api.utils.CharGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The implementation of a {@link CharGrid}
 */
public class ImplCharGrid implements CharGrid {
    private final List<String> tab = new ArrayList<>();
    private final int[] minCoords = {Integer.MAX_VALUE, Integer.MAX_VALUE};

    @Override
    public void setChar(int x, int y, char chr) {
        if (minCoords[1] == Integer.MAX_VALUE) {
            tab.add(" ");
            minCoords[0] = x;
            minCoords[1] = y;
        }
        else if (y < minCoords[1]) {
            int toAdd = minCoords[1] - y;
            for (int i = 0; i < toAdd; ++i) {
                tab.add(0, " ".repeat(tab.get(tab.size() - 1).length()));
            }
            minCoords[1] = y;
        }
        int relativeY = y - minCoords[1];
        for (int i = tab.size(); i <= relativeY; ++i) tab.add(" ".repeat(tab.get(0).length()));

        if (x < minCoords[0]) {
            int toAdd = minCoords[0] - x;
            tab.replaceAll(s -> " ".repeat(toAdd) + s);
            minCoords[0] = x;
        }
        int relativeX = x - minCoords[0];
        if (relativeX >= getDimensions()[0]) {
            for (int i = 0; i < tab.size(); ++i) {
                StringBuilder builder = new StringBuilder(tab.get(i));
                builder.append(" ".repeat(Math.max(0, relativeX - builder.length()) + 1));
                tab.set(i, builder.toString());
            }
        }

        StringBuilder builder = new StringBuilder(tab.get(relativeY));
        builder.setCharAt(relativeX, chr);
        tab.set(relativeY, builder.toString());
    }

    @Override
    public Optional<Character> getChar(int x, int y) {
        int[] dimensions = getDimensions();
        int relativeX = x - minCoords[0];
        int relativeY = y - minCoords[1];
        if (
            relativeX < 0 || relativeX >= dimensions[0]
                || relativeY < 0 || relativeY >= dimensions[1]
        ) return Optional.empty();
        return Optional.of(tab.get(y - minCoords[1]).charAt(x - minCoords[0]));
    }

    @Override
    public void addSidePanel(char separator, SidePanelDirection direction, CharGrid panel) {
        int[] maxDimensions = {
            Math.max(getDimensions()[0], panel.getDimensions()[0]),
            Math.max(getDimensions()[1], panel.getDimensions()[1])
        };
        String separatorString = (separator + (direction.isVertical() ? "" : "\n"))
            .repeat(maxDimensions[direction.isVertical() ? 0 : 1]);
        int sepX;
        switch (direction) {
            case RIGHT:
                sepX = minCoords[0] + getDimensions()[0];
                break;
            case LEFT:
                sepX = minCoords[0] - 1;
                break;
            default:
                sepX = minCoords[0];
        }
        int sepY;
        switch (direction) {
            case RIGHT:
            case LEFT:
                sepY = minCoords[1];
                break;
            case BOTTOM:
                sepY = minCoords[1] + getDimensions()[1];
                break;
            default:
                sepY = minCoords[1] - 1;
        }
        insertString(sepX, sepY, separatorString);
        int panelX;
        switch (direction) {
            case RIGHT:
                panelX = minCoords[0] + getDimensions()[0];
                break;
            case LEFT:
                panelX = minCoords[0] - panel.getDimensions()[0];
                break;
            default:
                panelX = minCoords[0];
        }
        int panelY;
        switch (direction) {
            case RIGHT:
            case LEFT:
                panelY = minCoords[1];
                break;
            case TOP:
                panelY = minCoords[1] - panel.getDimensions()[1];
                break;
            default:
                panelY = minCoords[1] + getDimensions()[1];
        }
        insert(panelX, panelY, panel);
    }

    @Override
    public void addSidePanel(SidePanelDirection direction, CharGrid panel) {
        addSidePanel(!direction.isVertical() ? '|' : '-', direction, panel);
    }

    @Override
    public void addSidePanel(CharGrid panel) {
        addSidePanel(SidePanelDirection.RIGHT, panel);
    }

    @Override
    public void insert(int x, int y, CharGrid inserted) {
        int[] dimensions = inserted.getDimensions();
        int[] insertedMinCoords = inserted.getMinCoords();
        for (int i = 0; i < dimensions[1]; ++i) {
            for (int j = 0; j < dimensions[0]; ++j) {
                int finalJ = j, finalI = i;
                inserted.getChar(insertedMinCoords[0] + j, insertedMinCoords[1] + i)
                    .ifPresent(
                        c -> setChar(x + finalJ, y + finalI, c)
                );
            }
        }
    }

    @Override
    public void insertString(int x, int y, String str) {
        String[] splitString = str.split("\n");
        for (int i = 0; i < splitString.length; ++i) {
            for (int j = 0; j < splitString[i].length(); ++j) {
                setChar(x + j, y + i, splitString[i].charAt(j));
            }
        }
    }

    @Override
    public int[] getDimensions() {
        return new int[] {tab.get(0).length(), tab.size()};
    }

    @Override
    public int[] getMinCoords() {
        return minCoords;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        tab.forEach(
            s -> {
                StringBuilder sBuilder = new StringBuilder();
                for (char chr : s.toCharArray()) sBuilder.append(chr).append(' ');
                builder.append(sBuilder).append('\n');
            }
        );
        return builder.toString().replaceAll("\n$", "");
    }
}
