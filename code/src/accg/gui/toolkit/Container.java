package accg.gui.toolkit;

import java.util.ArrayList;
import java.util.Collection;

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
}
