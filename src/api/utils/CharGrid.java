package api.utils;

import java.util.Optional;

/**
 * A utility class that can display characters on a grid, and automatically adapts to the required size
 */
public interface CharGrid {
    /**
     * Sets the character at the required coordinates to another one
     * @param x The x coordinate in the grid
     * @param y The y coordinate in the grid
     * @param chr The character to set
     */
    void setChar(int x, int y, char chr);

    /**
     * Returns an optional of the character at the given coordinates
     * @param x The x coordinate
     * @param y The y coordinate
     * @return An Optional containing the Character at the given coordinates, or an empty Optional if there is no
     * Character there
     */
    Optional<Character> getChar(int x, int y);

    /**
     * Adds a CharGrid to the side of this CharGrid
     * @param separator The separator to use
     * @param direction The direction to set the side panel to
     * @param panel The CharGrid displayed in the side panel
     */
    void addSidePanel(char separator, SidePanelDirection direction, CharGrid panel);

    /**
     * An overload of {@link CharGrid#addSidePanel(char, SidePanelDirection, CharGrid)} that creates a side panel
     * with either '|' or '-' as a separator, depending on the direction
     * @param direction The direction to set the side panel to
     * @param panel The CharGrid displayed in the side panel
     */
    void addSidePanel(SidePanelDirection direction, CharGrid panel);

    /**
     * An overload of {@link CharGrid#addSidePanel(SidePanelDirection, CharGrid)} that creates a side panel
     * with '|' as a separator, to the right
     * @param panel The CharGrid displayed in the side panel
     */
    void addSidePanel(CharGrid panel);

    /**
     * Inserts a CharGrid inside this one
     * @param x The x coordinate of the top left of the inserted CharGrid
     * @param y The y coordinate of the top left of the inserted CharGrid
     * @param inserted The CharGrid to insert
     */
    void insert(int x, int y, CharGrid inserted);

    /**
     * Inserts a String in this CharGrid. Newline character will insert in the next line of the CharGrid
     * @param x The x coordinate to place the String to
     * @param y The y coordinate to place the String to
     * @param str The String to insert
     */
    void insertString(int x, int y, String str);

    /**
     * @return The current dimensions of this CharGrid
     */
    int[] getDimensions();

    /**
     * @return The minimum coordinates of a character in each axis
     */
    int[] getMinCoords();

    /**
     * An enum used to select the direction to add a side panel to
     */
    enum SidePanelDirection {
        RIGHT,
        LEFT,
        BOTTOM,
        TOP;

        /**
         * @return {@code true} if the direction of the side panel is a vertical direction (top or bottom),
         * {@code false} otherwise
         */
        public boolean isVertical() {
            return this == TOP || this == BOTTOM;
        }
    }
}
