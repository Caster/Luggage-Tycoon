package accg.gui.toolkit.event;


/**
 * Event that indicates that the mouse has entered the component.
 */
public class MouseEnterEvent extends MouseEvent {

	protected int x;
	protected int y;
	
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
