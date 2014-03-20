package accg.gui.toolkit.event;

/**
 * Event that indicates that a mouse-click has occurred in the component.
 */
public class MouseClickEvent extends MouseEvent {
	
	/**
	 * The x-coordinate of this mouse click.
	 */
	protected int x;
	
	/**
	 * The y-coordinate of this mouse click.
	 */
	protected int y;
	
	/**
	 * Creates a new MouseClickEvent.
	 * 
	 * @param x The x-coordinate of this mouse click.
	 * @param y The y-coordinate of this mouse click.
	 */
	public MouseClickEvent(int x, int y) {
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
