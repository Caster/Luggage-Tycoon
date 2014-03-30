package accg.gui.toolkit.containers;

import java.util.ArrayList;

import accg.gui.toolkit.Component;
import accg.gui.toolkit.Container;

/**
 * A layered pane is a component that stacks various components on top of each
 * other. All components fill the entire area of the layered pane.
 */
public class LayeredPane extends Container {
	
	/**
	 * The components in this layered pane.
	 */
	private ArrayList<Component> components;
	
	/**
	 * Creates a new LayeredPane without components.
	 */
	public LayeredPane() {
		this.components = new ArrayList<>();
	}
	
	@Override
	public int getPreferredWidth() {
		
		int maxWidth = 0;
		
		for (Component c : components) {
			int width = c.getPreferredWidth();
			
			if (width > maxWidth) {
				maxWidth = width;
			}
		}
		
		return maxWidth;
	}

	@Override
	public int getPreferredHeight() {
		
		int maxHeight = 0;
		
		for (Component c : components) {
			int height = c.getPreferredHeight();
			
			if (height > maxHeight) {
				maxHeight = height;
			}
		}
		
		return maxHeight;
	}

	@Override
	public String getComponentName() {
		return "LayeredPane";
	}

	@Override
	public void add(Component toAdd) {
		toAdd.setParent(this);
		components.add(toAdd);
	}

	@Override
	public void layout() {
		for (Component c : components) {
			c.setX(getX());
			c.setY(getY());
			c.setWidth(getWidth());
			c.setHeight(getHeight());
		}
	}

	@Override
	public java.util.List<? extends Component> getChildren() {
		return components;
	}

	@Override
	protected boolean isTransparent() {
		return true;
	}
}
