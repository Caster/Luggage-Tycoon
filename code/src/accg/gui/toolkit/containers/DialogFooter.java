package accg.gui.toolkit.containers;

import java.util.ArrayList;

import accg.gui.toolkit.Component;
import accg.gui.toolkit.Container;

/**
 * A list of buttons (or components in general), shown next to each other.
 * 
 * \warning You probably do not want to use this class separately. Instead
 * you can use a Dialog.
 */
public class DialogFooter extends Container {

	/**
	 * The components shown.
	 */
	private ArrayList<Component> buttons;

	/**
	 * Distance between the buttons.
	 */
	public static final int PADDING = 10;
	
	/**
	 * Creates a new dialog footer.
	 */
	public DialogFooter() {
		this.buttons = new ArrayList<>();
	}
	
	@Override
	public void add(Component toAdd) {
		toAdd.setParent(this);
		buttons.add(toAdd);
		needsLayout();
	}

	@Override
	public void layout() {
		
		int x = getWidth();
		
		// start with the rightmost button
		for (int i = buttons.size() - 1; i >= 0; i--) {
			Component c = buttons.get(i);
			c.setWidth(c.getPreferredWidth());
			c.setHeight(c.getPreferredHeight());
			c.setX(x - c.getWidth());
			c.setY(0);
			x -= c.getWidth() + PADDING;
		}
	}

	@Override
	public int getPreferredWidth() {
		
		// take the sum of the widths of all buttons (plus paddings)
		int width = 0;
		
		for (Component c : buttons) {
			width += c.getPreferredWidth() + PADDING;
		}
		
		if (width > PADDING) {
			width -= PADDING;
		}
		
		return width;
	}

	@Override
	public int getPreferredHeight() {
		
		// take the height of the highest button
		int maxHeight = 0;
		
		for (Component c : buttons) {
			int height = c.getPreferredHeight();
			
			if (height > maxHeight) {
				maxHeight = height;
			}
		}
		
		return maxHeight;
	}

	@Override
	public java.util.List<? extends Component> getChildren() {
		return buttons;
	}

	@Override
	protected boolean isTransparent() {
		return true;
	}

	@Override
	public String getComponentName() {
		return "DialogFooter";
	}
}
