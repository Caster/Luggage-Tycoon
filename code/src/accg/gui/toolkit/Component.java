package accg.gui.toolkit;

import java.io.PrintStream;
import java.util.ArrayList;

import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Font;

/**
 * A component (widget) in the GUI. This is the superclass of all GUI
 * components.
 */
public abstract class Component {
	
	/**
	 * The bounds to draw this component in.
	 * 
	 * The coordinate system is relative to the parent.
	 */
	protected Rectangle outline = new Rectangle();
	
	/**
	 * The font to draw this component with.
	 */
	protected Font font;
	
	/**
	 * The parent of this component, or <code>null</code> if it has no parent.
	 * 
	 * TODO This should be {@link Container}, when that is implemented.
	 */
	protected Component parent;
	
	/**
	 * Whether this component is visible (<code>true</code>) or not
	 * (<code>false</code>).
	 */
	protected boolean visible = true;
	
	/**
	 * The list of listeners.
	 */
	protected ArrayList<Listener> listeners = new ArrayList<>();

	/**
	 * Returns the bounds of this component.
	 * 
	 * @return The bounds of this component.
	 */
	public Rectangle getOutline() {
		return outline;
	}

	/**
	 * Sets the X-coordinate of the upper-left-corner of this component.
	 * 
	 * @param x The new X-coordinate of this component.
	 */
	public void setX(int x) {
		outline.setX(x);
	}
	
	/**
	 * Returns the X-coordinate of the upper-left-corner of this component.
	 * 
	 * @return The X-coordinate of this component.
	 */
	public int getX() {
		return outline.getX();
	}

	/**
	 * Sets the Y-coordinate of the upper-left-corner of this component.
	 * 
	 * @param y The new Y-coordinate of this component.
	 */
	public void setY(int y) {
		outline.setY(y);
	}
	
	/**
	 * Returns the Y-coordinate of the upper-left-corner of this component.
	 * 
	 * @return The Y-coordinate of this component.
	 */
	public int getY() {
		return outline.getY();
	}

	/**
	 * Sets the width of this component.
	 * 
	 * @param width The new width of this component.
	 */
	public void setWidth(int width) {
		outline.setWidth(width);
	}
	
	/**
	 * Returns the width of this component.
	 * 
	 * @return The width in pixels of this component.
	 */
	public int getWidth() {
		return outline.getWidth();
	}

	/**
	 * Sets the height of this component.
	 * 
	 * @param height The new height of this component.
	 */
	public void setHeight(int height) {
		outline.setHeight(height);
	}
	
	/**
	 * Returns the height of this component.
	 * 
	 * @return The height in pixels of this component.
	 */
	public int getHeight() {
		return outline.getHeight();
	}
	
	/**
	 * Returns whether the given point lies inside or outside the component.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <code>true</code> if the point is contained by the component,
	 * <code>false</code> otherwise.
	 */
	public boolean contains(int x, int y) {
		return outline.contains(x, y);
	}
	
	/**
	 * Returns the preferred width of this component.
	 * 
	 * @return The preferred width in pixels of this component.
	 */
	public abstract int getPreferredWidth();
	
	/**
	 * Returns the preferred height of this component.
	 * 
	 * @return The preferred height in pixels of this component.
	 */
	public abstract int getPreferredHeight();
	
	/**
	 * Changes the parent of this component.
	 * 
	 * <b>Note:</b> This method is for internal use. Please don't use it
	 * if you do not know what you are doing.
	 * 
	 * @param parent The parent of this component.
	 */
	public void setParent(Component parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns the parent of this component.
	 * @return The parent of this component.
	 */
	public Component getParent() {
		return parent;
	}
	
	/**
	 * Changes the font used for this component.
	 * 
	 * @param font New font for this component.
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * Returns the font used for this component.
	 * @return The font.
	 */
	public Font getFont() {
		return (font == null ? parent.getFont() : font);
	}
	
	/**
	 * Shows or hides this component.
	 * @param visible Whether the component should be visible.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Returns whether this component is visible.
	 * @return The visibility.
	 */
	public boolean getVisible() {
		return visible;
	}
	
	/**
	 * Render this GUI element in the given outline using static
	 * OpenGL functions for immediate mode rendering.
	 */
	public abstract void draw();
	
	/**
	 * Adds a listener to the component. The listener will be notified
	 * of events.
	 * 
	 * @param l The listener to add.
	 */
	public void addListener(Listener l) {
		listeners.add(l);
	}
	
	/**
	 * Removes a listener from the component. The listener will not be
	 * notified anymore of events.
	 * 
	 * @param l The listener to add.
	 */
	public void removeListener(Listener l) {
		listeners.remove(l);
	}
	
	/**
	 * Sends an event to all of the listeners attached to this component.
	 * 
	 * @param e The event to send.
	 * @return Whether the event has been consumed.
	 */
	public boolean sendEvent(Event e) {
		
		// TODO remove this debug output
		System.out.print("*** " + e.getClass().getSimpleName() + " on ");
		String className = this.getClass().getSimpleName();
		if (className.equals(getComponentName())) {
			System.out.print(className);
		} else {
			System.out.print(className + " (= " + getComponentName() + ")");
		}
		System.out.println(" ***");
		
		boolean consumed = false;
		
		for (Listener l : listeners) {
			consumed = consumed || l.event(e);
		}
		
		return consumed;
	}
	
	public abstract String getComponentName();
	
	public void outputDebug() {
		outputDebug(System.out);
	}
	
	public void outputDebug(PrintStream out) {
		outputDebug(out, 0);
	}
	
	public void outputDebug(PrintStream out, int indent) {
		for (int i = 0; i < indent; i++) {
			out.print(" ");
		}
		
		String className = this.getClass().getSimpleName();
		if (className.equals(getComponentName())) {
			out.print(className);
		} else {
			out.print(className + " (= " + getComponentName() + ")");
		}
		out.print(";  ");
		out.print("pos: (" + getX() + ", " + getY() + ")");
		out.print(";  ");
		out.print("size: " + getWidth() + " x " + getHeight() + "");
		out.println();
		
		if (this instanceof Container) {
			for (Component c : ((Container) this).getChildren()) {
				c.outputDebug(out, indent + 2);
			}
		}
	}
}
