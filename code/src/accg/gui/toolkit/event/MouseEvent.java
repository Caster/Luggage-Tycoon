package accg.gui.toolkit.event;

import accg.gui.toolkit.Event;

/**
 * Representation of a mouse event.
 */
public abstract class MouseEvent extends Event {

	public abstract int getX();
	
	public abstract int getY();
	
	public abstract void translate(int dx, int dy);
}
