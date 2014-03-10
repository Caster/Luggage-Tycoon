package accg.gui.toolkit;

/**
 * A GUI container. This is a component that can contain other components,
 * and is responsible for layouting them.
 */
public abstract class Container extends Component {
	
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
	 * Flags that the outline needs to be recalculated.
	 */
	public void needsLayout() {
		outline = null;
	}
	
	/**
	 * If flagged by {@link #needsLayout()} before, layout this container
	 * (by calling {@link #layout()}).
	 */
	public void layoutIfNeeded() {
		if (outline == null) {
			layout();
		}
	}
}
