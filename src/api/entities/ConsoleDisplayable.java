package api.entities;

/**
 * An interface describing an element that can be converted to a char to be displayed on the console
 */
public interface ConsoleDisplayable {
    /**
     * @return The character representation of the object
     */
    char toChar();
}
