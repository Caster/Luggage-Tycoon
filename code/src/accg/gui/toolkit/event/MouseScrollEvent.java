package accg.gui.toolkit.event;

/**
 * Event that indicates that a mouse scroll has occurred in the component.
 */
public class MouseScrollEvent extends MouseEvent {
	
	/**
	 * The amount of scroll steps.
	 */
	protected int dWheel;
	
	/**
	 * The x-coordinate of this mouse scroll.
	 */
	protected int x;
	
	/**
	 * The y-coordinate of this mouse scroll.
	 */
	protected int y;

	/**
	 * Creates a new MouseMoveEvent.
	 * 
	 * @param x The x-coordinate of this mouse move.
	 * @param y The y-coordinate of this mouse move.
	 * @param dWheel The amount of scroll steps.
	 */
	public MouseScrollEvent(int x, int y, int dWheel) {
		this.x = x;
		this.y = y;
		this.dWheel = dWheel;
	}
	
	/**
	 * Returns the amount of scroll steps of this scroll event.
	 * @return The amount of scroll steps.
	 */
	public int getdWheel() {
		return dWheel;
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
