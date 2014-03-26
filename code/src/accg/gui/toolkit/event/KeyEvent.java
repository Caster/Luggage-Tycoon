package accg.gui.toolkit.event;

import accg.gui.toolkit.Event;

/**
 * Event that indicates that a key has been pressed.
 */
public class KeyEvent extends Event {
	
	/**
	 * The character of the key pressed.
	 */
	protected char c;
	
	/**
	 * Creates a new MouseClickEvent.
	 * 
	 * @param c The character of the key pressed.
	 */
	public KeyEvent(char c) {
		this.c = c;
	}
	
	/**
	 * Returns the key that was pressed.
	 * @return The character of the key pressed.
	 */
	public char getKey() {
		return c;
	}
}
