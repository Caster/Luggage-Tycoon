package accg.gui.toolkit.event;

import accg.gui.toolkit.Event;

/**
 * Event that indicates that a mouse-click has occurred in the component.
 */
public class MouseClickEvent extends Event {
	
	protected int x;
	protected int y;
	
	public MouseClickEvent(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
