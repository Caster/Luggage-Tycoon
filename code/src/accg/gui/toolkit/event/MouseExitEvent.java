package accg.gui.toolkit.event;

/**
 * Event that indicates that the mouse has exited the component.
 */
public class MouseExitEvent extends MouseEvent {
	
	/**
	 * The x-coordinate of this mouse exit.
	 */
	protected int x;
	
	/**
	 * The y-coordinate of this mouse exit.
	 */
	protected int y;
	
	/**
	 * Creates a new MouseExitEvent.
	 * 
	 * @param x The x-coordinate of this mouse exit.
	 * @param y The y-coordinate of this mouse exit.
	 */
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
