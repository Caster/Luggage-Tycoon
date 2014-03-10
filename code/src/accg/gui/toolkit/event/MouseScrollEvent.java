package accg.gui.toolkit.event;

import accg.gui.toolkit.Event;

/**
 * Event that indicates that a mouse-click has occurred in the component.
 */
public class MouseScrollEvent extends Event {
	
	protected int dWheel;
	
	public MouseScrollEvent(int dWheel) {
		this.dWheel = dWheel;
	}
	
	public int getdWheel() {
		return dWheel;
	}
}
