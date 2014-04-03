package accg.gui.toolkit.event;

import accg.gui.toolkit.Event;

/**
 * Event that indicates that a key has been pressed.
 */
public class KeyEvent extends Event {
	
	/**
	 * The LWJGL key code of the pressed key.
	 */
	protected int keyCode;
	
	/**
	 * The character of the key pressed.
	 */
	protected char c;
	
	/**
	 * Creates a new MouseClickEvent.
	 * 
	 * @param keyCode The LWJGL key code of the pressed key.
	 * @param c The character of the key pressed.
	 */
	public KeyEvent(int keyCode, char c) {
		this.keyCode = keyCode;
		this.c = c;
	}
	
	/**
	 * Returns the LWJGL key code of the pressed key.
	 * @return The key code.
	 */
	public int getKeyCode() {
		return keyCode;
	}
	
	/**
	 * Returns the key that was pressed.
	 * @return The character of the key pressed.
	 */
	public char getCharacter() {
		return c;
	}
}
