package accg.gui.toolkit.containers;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import accg.gui.toolkit.Component;
import accg.gui.toolkit.Container;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.enums.Orientation;

/**
 * A MenuBar is a menu with zero or more {@link Button}s in it.
 * 
 * It is drawn on top of the scene, in 2D. It is capable of handling
 * mouse events to make for a nice experience.
 * 
 * A MenuBar can have one child, that is also drawn by this MenuBar.
 */
public class MenuBar extends Container {
	
	/**
	 * Items in this menu bar.
	 */
	protected ArrayList<Button> items;
	
	/**
	 * The orientation of this menu bar: horizontal or vertical.
	 */
	protected Orientation orientation = Orientation.HORIZONTAL;

	/**
	 * The preferred width; set in {@link #layout()}.
	 */
	protected int preferredWidth;

	/**
	 * The preferred height; set in {@link #layout()}.
	 */
	protected int preferredHeight;
	
	/**
	 * Constructs a new {@link MenuBar} at the given position.
	 */
	public MenuBar() {
		this.items = new ArrayList<>();
	}
	
	@Override
	public int getPreferredWidth() {
		layoutIfNeeded();
		return preferredWidth;
	}

	@Override
	public int getPreferredHeight() {
		layoutIfNeeded();
		return preferredHeight;
	}
	
	/**
	 * Handles a change in locale by updating the texts in all menu items.
	 */
	public void handleLocaleChanged() {
		for (Button b : items) {
			b.handleLocaleChanged();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * This makes sure that all items fit in the menu bar,
	 * which is positioned correctly with respect to
	 * the position and outline of the menu bar. Note that in case the position
	 * is either top or bottom, all items should be placed next to each other.
	 * Otherwise, all items should be placed below each other.
	 */
	@Override
	public void layout() {
		
		switch (orientation) {
		default:
		case HORIZONTAL:
			// the extraWidth is the additional space divided over all items
			int totalItemWidth = 0;
			int maxHeight = 0;
			for (Button item : items) {
				totalItemWidth += item.getPreferredWidth();
				if (item.getPreferredHeight() > maxHeight) {
					maxHeight = item.getPreferredHeight();
				}
			}
			int extraWidth = (getWidth() - totalItemWidth) / items.size();
			
			// store the preferred size
			preferredWidth = totalItemWidth;
			preferredHeight = maxHeight;
			
			// now actually layout the items
			int x = 0;
			for (Button item : items) {
				item.setX(x);
				item.setY(0);
				item.setWidth(item.getPreferredWidth() + extraWidth);
				item.setHeight(getHeight());
				x += item.getPreferredWidth() + extraWidth;
			}
			break;
		case VERTICAL:
			// the extraHeight is the additional space divided over all items
			int totalItemHeight = 0;
			int maxWidth = 0;
			for (Button item : items) {
				totalItemHeight += item.getPreferredHeight();
				if (item.getPreferredWidth() > maxWidth) {
					maxWidth = item.getPreferredWidth();
				}
			}
			int extraHeight = (getHeight() - totalItemHeight) / items.size();
			
			// store the preferred size
			preferredWidth = maxWidth;
			preferredHeight = totalItemHeight;
			
			// now actually layout the items
			int y = 0;
			for (Button item : items) {
				item.setX(0);
				item.setY(y);
				item.setWidth(getWidth());
				item.setHeight(item.getPreferredHeight() + extraHeight);
				y += item.getPreferredHeight() + extraHeight;
			}
			break;
		}
	}

	@Override
	public void draw() {
		
		// draw menu bar background, which is semi-transparent
		glColor4d(1, 1, 1, 0.5);
		glBegin(GL_QUADS);
		{
			drawBackground();
		}
		glEnd();
		
		super.draw();
	}

	@Override
	public ArrayList<? extends Component> getChildren() {
		return items;
	}
	
	@Override
	public String getComponentName() {
		return "MenuBar";
	}
	
	/**
	 * Set the <code>isChecked</code> property of all menu items in this menu to
	 * false, except for the given item.
	 * 
	 * @param exceptFor The item of which the <code>isChecked</code> property
	 * should not be changed.
	 */
	public void uncheckOtherItems(Button exceptFor) {
		for (Button item : items) {
			if (item != exceptFor) {
				item.setChecked(false);
			}
		}
	}
	
	/**
	 * Assumes that it is called inside GL_QUADS. Will render a background
	 * on the correct spot.
	 */
	private void drawBackground() {
		glVertex2d(0, outline.getHeight());
		glVertex2d(outline.getWidth(), outline.getHeight());
		glVertex2d(outline.getWidth(), 0);
		glVertex2d(0, 0);
	}

	@Override
	public void add(Component toAdd) {
		if (!(toAdd instanceof Button)) {
			throw new IllegalArgumentException("Can only add MenuBarItems to a MenuBar");
		}
		
		Button item = (Button) toAdd;
		
		items.add(item);
		item.setParent(this);
		
		needsLayout();
	}

	@Override
	protected boolean isTransparent() {
		return false;
	}
	
	/**
	 * Sets the orientation of this menu bar.
	 * @param orientation The new orientation.
	 */
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
		
		needsLayout();
	}
	
	/**
	 * Returns the orientation of this menu bar.
	 * @return The orientation.
	 */
	public Orientation getOrientation() {
		return orientation;
	}
}
