package accg.gui.toolkit;

import java.util.Collection;

import accg.gui.toolkit.event.MouseEvent;

/**
 * A GUI container. This is a component that can contain other components,
 * and is responsible for layouting them.
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
	
	/**
	 * Returns a list of all children.
	 * 
	 * The resulting list may not be modified; else behaviour is undefined.
	 * 
	 * @return A list of the children of this container.
	 */
	public abstract Collection<? extends Component> getChildren();
	
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
	 * @return Whether the container is transparent (<code>true</code>) or
	 * not (<code>false</code>).
	 */
	protected abstract boolean isTransparent();
	
	/**
	 * Flags that the outline needs to be recalculated.
	 */
	public void needsLayout() {
		needsLayout = true;
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
		
		for (Component c : getChildren()) {
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
			}
			if (e instanceof MouseEvent) {
				((MouseEvent) e).translate(c.getX(), c.getY());
			}
		}
		
		// handle the event ourselves
		if (!handled) {
			super.sendEvent(e);
		}
		
		if (isTransparent()) {
			return handled;
		}
		return true;
	}
}
