package api.events.gui;

import api.entities.GuiGlobalDisplayable;
import api.events.Event;

/**
 * An event called when a {@link GuiGlobalDisplayable} starts being displayed
 */
public class EnterDisplayableViewEvent extends Event {
    private final GuiGlobalDisplayable displayable;

    public EnterDisplayableViewEvent(GuiGlobalDisplayable displayable) {
        this.displayable = displayable;
    }

    public GuiGlobalDisplayable getDisplayable() {
        return displayable;
    }

    @Override
    public String toString() {
        return "Entered displayable view of type " + getDisplayable().getClass().getSimpleName();
    }
}
