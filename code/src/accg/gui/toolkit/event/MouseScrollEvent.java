package accg.gui.toolkit.event;

import accg.gui.toolkit.Event;

/**
 * Event that indicates that a mouse scroll has occurred in the component.
 */
public class MouseScrollEvent extends MouseEvent {
	
	protected int dWheel;
	
	protected int x;
	protected int y;
	
	public MouseScrollEvent(int x, int y, int dWheel) {
		this.x = x;
		this.y = y;
		this.dWheel = dWheel;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	public int getdWheel() {
		return dWheel;
	}

	@Override
	public void translate(int dx, int dy) {
		x += dx;
		y += dy;
	}
}
