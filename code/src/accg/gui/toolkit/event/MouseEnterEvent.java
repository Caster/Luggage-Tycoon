package accg.gui.toolkit.event;

/**
 * Event that indicates that the mouse has entered the component.
 */
public class MouseEnterEvent extends MouseEvent {
	
	/**
	 * The x-coordinate of this mouse enter.
	 */
	protected int x;
	
	/**
	 * The y-coordinate of this mouse enter.
	 */
	protected int y;
	
	/**
	 * Creates a new MouseEnterEvent.
	 * 
	 * @param x The x-coordinate of this mouse enter.
	 * @param y The y-coordinate of this mouse enter.
	 */
	public MouseEnterEvent(int x, int y) {
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
