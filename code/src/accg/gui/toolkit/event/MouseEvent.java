package accg.gui.toolkit.event;

import accg.gui.toolkit.Event;

/**
 * Representation of a mouse event.
 */
public abstract class MouseEvent extends Event {
	
	/**
	 * Returns the x-coordinate of this mouse event.
	 * @return The x-coordinate.
	 */
	public abstract int getX();

	/**
	 * Returns the y-coordinate of this mouse event.
	 * @return The y-coordinate.
	 */
	public abstract int getY();
	
	/**
	 * Translates this mouse event to a new origin.
	 * 
	 * @param dx The amount to translate in the x-direction.
	 * @param dy The amount to translate in the y-direction.
	 */
	public abstract void translate(int dx, int dy);
}
