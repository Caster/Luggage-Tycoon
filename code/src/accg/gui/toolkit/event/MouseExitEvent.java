package accg.gui.toolkit.event;

import accg.gui.toolkit.Event;

/**
 * Event that indicates that the mouse has exited the component.
 */
public class MouseExitEvent extends MouseEvent {

	protected int x;
	protected int y;
	
	public MouseExitEvent(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}

	@Override
	public void translate(int dx, int dy) {
		x += dx;
		y += dy;
	}
}
