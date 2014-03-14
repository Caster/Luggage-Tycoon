package accg.gui.toolkit;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

/**
 * A MenuBar is a menu with zero or more {@link MenuBarItem}s in it.
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
	protected ArrayList<MenuBarItem> items;
	
	/**
	 * The orientation of this menu bar: horizontal or vertical.
	 */
	protected Orientation orientation = Orientation.HORIZONTAL;
	
	/**
	 * Possible orientations for a {@link MenuBar}.
	 */
	public enum Orientation {
		/**
		 * A horizontal menu bar.
		 */
		HORIZONTAL,
		
		/**
		 * A vertical menu bar.
		 */
		VERTICAL
	}

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
			for (MenuBarItem item : items) {
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
			for (MenuBarItem item : items) {
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
			for (MenuBarItem item : items) {
				totalItemHeight += item.getPreferredHeight();
				if (item.getPreferredWidth() > maxWidth) {
					maxWidth = item.getPreferredWidth();
				}
			}
			int extraHeight = (getWidth() - totalItemHeight) / items.size();
			
			// store the preferred size
			preferredWidth = maxWidth;
			preferredHeight = totalItemHeight;
			
			// now actually layout the items
			int y = 0;
			for (MenuBarItem item : items) {
				item.setX(0);
				item.setY(y);
				item.setWidth(getWidth());
				item.setHeight(item.getPreferredHeight() + extraHeight);
				y += item.getPreferredHeight() + extraHeight;
			}
			break;
		}
		
		/*
		 * nog meer layoutcode:
		 * (voor verticaal, komt uit draw)
		 
			Rectangle itemOutline = new Rectangle();
			itemOutline.setX(outline.getX());
			itemOutline.setY(outline.getY() - outline.getHeight());
			itemOutline.setHeight(outline.getHeight() / items.size());
			itemOutline.setWidth(outline.getWidth());
			for (MenuBarItem item : items) {
				itemOutline.setY(itemOutline.getY() + itemOutline.getHeight());
				item.draw();
			}
		 */
		
		/*
		switch (pres) {
		default:
		case ICON_ABOVE_TEXT:
			itemHeight = font.getLineHeight() * 4 +
					3 * MenuBarItem.PADDING;
			break;
		case ICON_LEFT_TEXT:
			itemHeight = font.getLineHeight() +
					2 * MenuBarItem.PADDING;
		}
		
		// determine maximal width of all items and sum
		int maxWidth = 0, sumWidth = 0, itemWidth;
		for (MenuBarItem item : items) {
			itemWidth = item.getPreferredWidth(font, pos, pres);
			sumWidth += itemWidth;
			if (itemWidth > maxWidth) {
				maxWidth = itemWidth;
			}
		}
		
		// determine width and height of outline
		if (pos == Position.TOP || pos == Position.BOTTOM) {
			// all items are next to each other
			outline.setHeight(itemHeight);
			switch (pres) {
			default :
			case ICON_ABOVE_TEXT :
				outline.setWidth(items.size() * maxWidth);
				break;
			case ICON_LEFT_TEXT :
				outline.setWidth(sumWidth);
				break;
			}
		} else {
			// all items are below each other
			outline.setHeight(items.size() * itemHeight);
			outline.setWidth(maxWidth);
		}*/
	}

	@Override
	public void draw() {
		
		// should we draw in the first place? TODO is this a task for Component?
		if (!this.visible || items.size() == 0) {
			return;
		}
		
		// if needed, compute layout
		layoutIfNeeded();
		
		// apply the transformation
		glPushMatrix();
		glTranslatef(outline.getX(), outline.getY(), 0);
		
		// enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		// draw menu bar background, which is semi-transparent
		glColor4d(1, 1, 1, 0.5);
		glBegin(GL_QUADS);
		{
			drawBackground();
		}
		glEnd();
		
		// draw all items
		for (MenuBarItem item : items) {
			item.draw();
		}
		
		// disable transparency again
		glDisable(GL_BLEND);
		
		// restore the transformation
		glPopMatrix();
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
	 * Handle a mouse click event where the mouse is currently at the given
	 * position in window coordinates.
	 * 
	 * @param x X-coordinate of current mouse position.
	 * @param y Y-coordinate of current mouse position.
	 * @return If the event has been used or not.
	 */
	/*public boolean handleMouseClickEvent(int x, int y) {
		// our Y-axis is inverted, sadly, because of font rendering...
		y = Display.getHeight() - y;
		
		// We cheat here: if the mouse has clicked at a menu item, then that
		// menu item must be hovered right now. Therefore, we do not need
		// cumbersome look-up functions now: we just click the hovered item
		// if there is any at all!
		for (MenuBarItem item : items) {
			if (item.isHovered()) {
				item.sendEvent(new MouseClickEvent(x, y));
				return true;
			}
		}
		sendEvent(new MouseClickEvent(x, y));
		return false;
	}*/
	
	/**
	 * Handle a mouse move event where the mouse is currently at the given
	 * position in window coordinates.
	 * 
	 * @param x X-coordinate of current mouse position.
	 * @param y Y-coordinate of current mouse position.
	 * @return If the event has been used or not.
	 */
	/*public boolean handleMouseMoveEvent(int x, int y) {
		// do we even have items that can be hovered?
		if (!visible || items.size() == 0) {
			return false;
		}
		
		// our Y-axis is inverted, sadly, because of font rendering...
		y = Display.getHeight() - y;
		
		// determine outline
		Rectangle outline = getOutline();
		Position pos = getPosition();
		
		// check if any (and if yes, which) menu item has been clicked
		boolean handled = false;
		if (pos == Position.TOP || pos == Position.BOTTOM) {
			// check if the vertical position matches at least
			if (y < outline.getY() - outline.getHeight() ||
					y > outline.getY()) {
				for (MenuBarItem item : items) {
					item.setHovered(false);
				}
				return false;
			}
			
			// check if some horizontal position matches
			Rectangle itemOutline = new Rectangle();
			itemOutline.setX(outline.getX());
			itemOutline.setY(outline.getY());
			itemOutline.setHeight(outline.getHeight());
			itemOutline.setWidth(outline.getWidth() / items.size());
			for (MenuBarItem item : items) {
				// presentation-specific tweaks to itemOutline
				if (pres == Presentation.ICON_LEFT_TEXT) {
					itemOutline.setWidth(item.getPreferredWidth(
							font, pos, pres));
				}
				
				// check if we have a hit and handle it
				if (itemOutline.getX() <= x && x <= itemOutline.getX() +
						itemOutline.getWidth())	{
					item.setHovered(true);
					if (Mouse.isButtonDown(0)) {
						item.onDrag(x, y);
					}
					handled = true;
				} else {
					item.setHovered(false);
				}
				
				// move up the itemOutline
				itemOutline.setX(itemOutline.getX() + itemOutline.getWidth());
			}
		} else {
			// check if the horizontal position matches at least
			if (x < outline.getX() ||
					x > outline.getX() + outline.getWidth()) {
				for (MenuBarItem item : items) {
					item.setHovered(false);
				}
				return false;
			}
			
			// check if some vertical position matches
			Rectangle itemOutline = new Rectangle();
			itemOutline.setX(outline.getX());
			itemOutline.setY(outline.getY() - outline.getHeight());
			itemOutline.setHeight(outline.getHeight() / items.size());
			itemOutline.setWidth(outline.getWidth());
			for (MenuBarItem item : items) {
				if (itemOutline.getY() <= y && y <= itemOutline.getY() +
						itemOutline.getHeight()) {
					item.setHovered(true);
					if (Mouse.isButtonDown(0)) {
						item.onDrag(x, y);
					}
					handled = true;
				} else {
					item.setHovered(false);
				}
				itemOutline.setY(itemOutline.getY() + itemOutline.getHeight());
			}
		}
		
		return handled;
	}*/
	
	/**
	 * Handle a mouse wheel event where the mouse is currently at the given
	 * position in window coordinates.
	 * 
	 * @param x X-coordinate of current mouse position.
	 * @param y Y-coordinate of current mouse position.
	 * @param dWheel The delta of the mouse wheel.
	 * @return If the event has been used or not.
	 */
	/*public boolean handleMouseWheelEvent(int x, int y, int dWheel) {
		// We cheat here: if the mouse is over a menu item, then that
		// menu item must be hovered right now. Therefore, we do not need
		// cumbersome look-up functions now: we just wheel the hovered item
		// if there is any at all!
		for (MenuBarItem item : items) {
			if (item.isHovered()) {
				item.onScroll(dWheel);
				return true;
			}
		}
		return false;
	}*/
	
	/**
	 * Handle a resize event of the display. That is, make sure that the
	 * menu bar is still positioned at the edge of the display.
	 * 
	 * @param width New width of the display.
	 * @param height New height of the display.
	 */
	/*public void handleResizeEvent(int width, int height) {
		needsLayout();
	}*/
	
	/**
	 * Set the <code>isChecked</code> property of all menu items in this menu to
	 * false, except for the given item.
	 * 
	 * @param exceptFor The item of which the <code>isChecked</code> property
	 * should not be changed.
	 */
	public void uncheckOtherItems(MenuBarItem exceptFor) {
		for (MenuBarItem item : items) {
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
		if (!(toAdd instanceof MenuBarItem)) {
			throw new IllegalArgumentException("Can only add MenuBarItems to a MenuBar");
		}
		
		MenuBarItem item = (MenuBarItem) toAdd;
		
		items.add(item);
		item.setParent(this);
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
	}
	
	/**
	 * Returns the orientation of this menu bar.
	 * @return The orientation.
	 */
	public Orientation getOrientation() {
		return orientation;
	}
}
