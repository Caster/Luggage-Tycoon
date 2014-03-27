package accg.gui.toolkit;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import accg.gui.toolkit.event.MouseEvent;
import accg.gui.toolkit.event.MouseMoveEvent;

/**
 * A GUI container. This is a component that can contain other components
 * (called its <i>children</i>), and is responsible for layouting them.
 * 
 * A container may not have overlapping children, else behaviour is undefined.
 */
public abstract class Container extends Component {
	
	/**
	 * Whether the outline needs to be recomputed.
	 */
	protected boolean needsLayout = true;
	
	/**
	 * Adds a new child to this container.
	 * 
	 * @param toAdd The component to add as a new child.
	 * @throws IllegalArgumentException If the child could not be added. Some
	 * components may have restrictions on what components are allowed; if those
	 * are not fulfilled, this exception is thrown.
	 */
	public abstract void add(Component toAdd);
	
	/**
	 * Computes and sets the positions and sizes of the children.
	 */
	public abstract void layout();
	
	@Override
	public void draw() {
		
		// if needed, compute layout
		layoutIfNeeded();
		
		// actually draw the children
		for (Component c : getChildren()) {
			
			// do not draw non-visible children
			if (!c.isVisible()) {
				continue;
			}
			
			// apply the transformation
			glPushMatrix();
			glTranslatef(c.getOutline().getX(), c.getOutline().getY(), 0);
			
			c.draw();
			
			// restore the transformation
			glPopMatrix();
		}
	}
	
	/**
	 * Returns a list of all children.
	 * 
	 * The resulting list may not be modified; else behaviour is undefined.
	 * 
	 * @return A list of the children of this container.
	 */
	public abstract java.util.List<? extends Component> getChildren();
	
	/**
	 * Whether this container is <i>transparent</i>.
	 * 
	 * When a container is non-transparent, events will not be passed on to
	 * parent containers, also if no child handled the event. (In that case,
	 * only the listeners of this container itself are notified.)
	 * 
	 * When a container is transparent, events will be passed on to the parent
	 * if no child handled them. In that case, the listeners of this container
	 * will not be notified.
	 * 
	 * <b>Note:</b> mouse move events will always be passed on to the parent
	 * container, regardless of whether the container is transparent or not.
	 * 
	 * @return Whether the container is transparent (<code>true</code>) or
	 * not (<code>false</code>).
	 */
	protected abstract boolean isTransparent();
	
	/**
	 * Flags that the container needs to be layouted again. This method should
	 * be called after every operation that may change the size of this component.
	 * This method will automatically also flag the parent of this container for
	 * layouting.
	 */
	public void needsLayout() {
		needsLayout = true;
		
		if (parent != null) {
			parent.needsLayout();
		}
	}
	
	/**
	 * If flagged by {@link #needsLayout()} before, layout this container
	 * (by calling {@link #layout()}).
	 */
	public void layoutIfNeeded() {
		if (needsLayout) {
			layout();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * This is overridden in {@link Container} since here we also check if we
	 * can relay the event to one of the children.
	 */
	@Override
	public boolean sendEvent(Event e) {
		
		boolean handled = false;
		
		List<? extends Component> children = getChildren();
		
		// iterate backwards, since we want the component on top first if there
		// are overlapping components
		for (int i = children.size() - 1; i >= 0; i--) {
			Component c = children.get(i);
			
			// do not relay events to non-visible children
			if (!c.isVisible()) {
				continue;
			}
			
			boolean shouldRelay = true;
			if (e instanceof MouseEvent) {
				// only relay mouse events if the mouse is over the object
				if (!c.getOutline().contains(((MouseEvent) e).getX(), ((MouseEvent) e).getY())) {
					shouldRelay = false;
				}
				((MouseEvent) e).translate(-c.getX(), -c.getY());
			}
			if (shouldRelay) {
				handled = c.sendEvent(e) || handled;
				if (handled) {
					break;
				}
			}
			if (e instanceof MouseEvent) {
				((MouseEvent) e).translate(c.getX(), c.getY());
			}
		}
		
		// handle the event ourselves
		if (!handled) {
			super.sendEvent(e);
		}
		
		// always relay MouseMoveEvents
		if (e instanceof MouseMoveEvent) {
			return false;
		}
		
		if (isTransparent()) {
			return handled;
		}
		return true;
	}
}
