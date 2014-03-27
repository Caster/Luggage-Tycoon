package accg.gui.toolkit;

import java.io.PrintStream;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Font;

import accg.gui.toolkit.event.MouseClickEvent;
import accg.gui.toolkit.event.MouseMoveEvent;

/**
 * A component (widget) in the GUI. This is the superclass of all GUI
 * components.
 */
public abstract class Component {
	
	/**
	 * If <code>true</code>, debug messages will be shown for every event.
	 */
	public static boolean DEBUG = false;
	
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
	 */
	protected Container parent;
	
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
	 * The element that currently has the keyboard focus.
	 */
	protected static Component focusElement = null;

	/**
	 * Returns the bounds of this component, in the coordinate system of its
	 * parent.
	 * 
	 * @return The bounds of this component.
	 */
	public Rectangle getOutline() {
		return outline;
	}

	/**
	 * Returns the bounds of this component, in the coordinate system of the
	 * highest ancestor (usually the window).
	 * 
	 * @return The bounds of this component, in window coordinates.
	 */
	public Rectangle getAbsoluteOutline() {
		
		Rectangle result = new Rectangle(outline);
		
		Component p = this;
		while ((p = p.getParent()) != null) {
			result.translate(p.getX(), p.getY());
		}
		return result;
	}
	
	/**
	 * Returns whether the given point lies inside or outside the component,
	 * in the coordinate system of its parent.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <code>true</code> if the point is contained by the component,
	 * <code>false</code> otherwise.
	 */
	public boolean contains(int x, int y) {
		return getOutline().contains(x, y);
	}
	
	/**
	 * Returns whether the given point lies inside or outside the component,
	 * in the coordinate system of the highest ancestor (usually the window).
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <code>true</code> if the point is contained by the component,
	 * <code>false</code> otherwise.
	 */
	public boolean absoluteContains(int x, int y) {
		return getAbsoluteOutline().contains(x, y);
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
	public void setParent(Container parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns the parent of this component.
	 * @return The parent of this component.
	 */
	public Container getParent() {
		return parent;
	}
	
	/**
	 * Returns the highest ancestor of this component; that is,
	 * <pre>
	 *     p.getParent().getParent(). ... . getParent();
	 * </pre>
	 * where <code>getParent()</code> is repeated until it is null.
	 * 
	 * @return The highest ancestor.
	 */
	public Component getHighestAncestor() {
		
		Component p = this;
		while (p.getParent() != null) {
			p = p.getParent();
		}
		
		return p;
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
	public boolean isVisible() {
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
	 * @return Whether the event has been handled by this component or a child of
	 * it.
	 */
	public boolean sendEvent(Event e) {
		
		if (DEBUG) {
			if (!(e instanceof MouseMoveEvent)) {
				System.out.print("*** " + e.getClass().getSimpleName() + " on ");
				String className = this.getClass().getSimpleName();
				if (className.equals(getComponentName())) {
					System.out.print(className);
				} else {
					System.out.print(className + " (= " + getComponentName() + ")");
				}
				System.out.println(" ***");
			}
		}
		
		for (Listener l : listeners) {
			l.event(e);
		}
		
		// return !e.shouldPropagate(); // TODO introduce API like this
		
		// temporary solution?
		if (e instanceof MouseClickEvent) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the name of the type of component. This should be just the class
	 * name of the component.
	 * 
	 * This is used for debugging purposes.
	 * 
	 * If you extend an existing component, you could override this method if
	 * it is a really new component.
	 * 
	 * @return The name of this component.
	 */
	public abstract String getComponentName();
	
	/**
	 * Outputs debug information about this component to standard output.
	 */
	public void outputDebug() {
		outputDebug(System.out);
	}

	/**
	 * Outputs debug information about this component to the given output
	 * stream.
	 * 
	 * @param out The stream to print the information to. This will not be
	 * closed afterwards.
	 */
	public void outputDebug(PrintStream out) {
		outputDebug(out, 0);
	}

	/**
	 * Outputs debug information about this component to the given output
	 * stream, with a given indentation level.
	 * 
	 * This is useful for printing "trees" of components and their children.
	 * 
	 * @param out The stream to print the information to. This will not be
	 * closed afterwards.
	 * @param indent The number of spaces to start with.
	 */
	protected void outputDebug(PrintStream out, int indent) {
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
	
	/**
	 * Returns the component that has currently the focus.
	 * 
	 * @return The focused element, or <code>null</code> if no element is
	 * currently focused.
	 */
	public static Component getKeyFocusElement() {
		return focusElement;
	}
	
	/**
	 * Requests the keyboard focus.
	 */
	public void requestFocus() {
		focusElement = this;
	}
	
	/**
	 * Returns whether this component has the keyboard focus.
	 * @return <code>true</code> if this has the keyboard focus;
	 * <code>false</code> otherwise.
	 */
	public boolean hasFocus() {
		return focusElement == this;
	}
	
	/**
	 * Returns whether this component is hovered.
	 * 
	 * @return <code>true</code> if the component is hovered; <code>false</code>
	 * otherwise.
	 */
	public boolean isHovered() {
		// TODO The getHighestAncestor().getHeight() part is a hack, but it
		// is unavoidable until we have some proper API for interacting with
		// LWJGL.
		return absoluteContains(Mouse.getX(), getHighestAncestor().getHeight() - Mouse.getY());
	}
}
