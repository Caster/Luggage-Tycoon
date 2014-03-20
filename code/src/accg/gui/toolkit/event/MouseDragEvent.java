package accg.gui.toolkit.event;

/**
 * Event that indicates that a mouse-click has occurred in the component.
 */
public class MouseDragEvent extends MouseEvent {
	
	/**
	 * The x-coordinate of this mouse drag.
	 */
	protected int x;
	
	/**
	 * The y-coordinate of this mouse drag.
	 */
	protected int y;
	
	/**
	 * Creates a new MouseDragEvent.
	 * 
	 * @param x The x-coordinate of this mouse drag.
	 * @param y The y-coordinate of this mouse drag.
	 */
	public MouseDragEvent(int x, int y) {
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
