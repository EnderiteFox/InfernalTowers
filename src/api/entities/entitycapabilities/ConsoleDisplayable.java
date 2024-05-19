package api.entities.entitycapabilities;

/**
 * An interface describing an element that can be turned into a char and displayed in the console
 */
public interface ConsoleDisplayable {
    /**
     * @return The character representation of the object
     */
    char toChar();
}
