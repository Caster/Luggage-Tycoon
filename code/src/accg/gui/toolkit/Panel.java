package accg.gui.toolkit;

import java.util.Collection;
import java.util.Collections;

/**
 * A panel is a component that contains one child. This child is then
 * layouted in some way.
 */
public class Panel extends Container {
	
	/**
	 * The component in this panel.
	 */
	private Component child;
	
	/**
	 * Creates a new panel.
	 * @param child The child.
	 */
	public Panel(Component child) {
		child.setParent(this);
		this.child = child;
	}
	
	@Override
	public int getPreferredWidth() {
		
		return child.getPreferredWidth();
	}

	@Override
	public int getPreferredHeight() {
		
		return child.getPreferredHeight();
	}

	@Override
	public String getComponentName() {
		return "Panel";
	}

	@Override
	public void add(Component toAdd) {
		throw new UnsupportedOperationException("Adding to a Panel is not supported; use setChild() instead");
	}

	@Override
	public void layout() {
		
		child.setWidth(child.getPreferredWidth());
		child.setHeight(child.getPreferredHeight());
		
		child.setX((getWidth() - child.getWidth()) / 2);
		child.setY((getHeight() - child.getHeight()) / 2);
	}

	@Override
	public Collection<? extends Component> getChildren() {
		return Collections.singleton(child);
	}

	@Override
	protected boolean isTransparent() {
		return false;
	}
}
