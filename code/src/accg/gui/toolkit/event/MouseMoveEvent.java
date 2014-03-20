package accg.gui.toolkit.event;

/**
 * Event that indicates that a mouse move has occurred in the component.
 */
public class MouseMoveEvent extends MouseEvent {
	
	/**
	 * The x-coordinate of this mouse move.
	 */
	protected int x;
	
	/**
	 * The y-coordinate of this mouse move.
	 */
	protected int y;
	
	/**
	 * Creates a new MouseMoveEvent.
	 * 
	 * @param x The x-coordinate of this mouse move.
	 * @param y The y-coordinate of this mouse move.
	 */
	public MouseMoveEvent(int x, int y) {
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
