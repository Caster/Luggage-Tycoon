package accg.gui;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

import accg.State;
import accg.objects.DrawableObject;

/**
 * A MenuBar is a menu with zero or more {@link MenuBarItem} in it.
 * 
 * It is drawn on top of the scene, in 2D. It is capable of handling
 * mouse events to make for a nice experience.
 */
public class MenuBar extends DrawableObject {

	/**
	 * Distance between edge of screen and the menu bar.
	 * 
	 * In case this is a submenu, the menu bar is drawn directly next
	 * to its parent.
	 */
	public static final int MARGIN = 10;
	/**
	 * Possible alignments for items inside the {@link MenuBar}.
	 * 
	 * Picking an alignment will move the menu bar to the according position
	 * on the screen.
	 */
	public enum Alignment {
		/** Left or top. */
		BEGIN,
		/** Center. */
		CENTER,
		/** Right or bottom. */
		END
	};
	/**
	 * Possible positions for a {@link MenuBar}.
	 */
	public enum Position {
		TOP, RIGHT, BOTTOM, LEFT
	};
	
	/**
	 * Construct a new {@link MenuBar} at the given position.
	 * 
	 * @param state State of the program, used to extract font from.
	 * @param pos On which side of the window the menu bar will stick.
	 * @param align The alignment of the menu bar on the given position.
	 */
	public MenuBar(State state, Position pos, Alignment align) {
		this.state = state;
		this.position = pos;
		this.alignment = align;
		this.items = new ArrayList<>();
	}
	
	/**
	 * Append the given item to the list of menu items in this menu bar.
	 * 
	 * @param item The menu item to be added.
	 */
	public void addMenuBarItem(MenuBarItem item) {
		if (!items.contains(item)) {
			items.add(item);
		}
	}
	
	@Override
	public void draw(State s) {
		// determine outline
		Rectangle outline = getOutline();
		
		// switch to 2D rendering
		GUI.make2D();
		
		// enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		// draw menu bar background, which is semi-transparent
		glColor4d(1, 1, 1, 0.5);
		glBegin(GL_QUADS);
		drawBackground(outline);
		glEnd();
		
		// draw menu items
		if (position == Position.TOP || position == Position.BOTTOM) {
			Rectangle itemOutline = new Rectangle();
			itemOutline.setX(outline.getX());
			itemOutline.setY(outline.getY());
			itemOutline.setHeight(outline.getHeight());
			itemOutline.setWidth(outline.getWidth() / items.size());
			for (MenuBarItem item : items) {
				item.draw(itemOutline, s.fontMenu);
				itemOutline.setX(itemOutline.getX() + itemOutline.getWidth());
			}
		} else {
			Rectangle itemOutline = new Rectangle();
			itemOutline.setX(outline.getX());
			itemOutline.setY(outline.getY() - outline.getHeight());
			itemOutline.setHeight(outline.getHeight() / items.size());
			itemOutline.setWidth(outline.getWidth());
			for (MenuBarItem item : items) {
				itemOutline.setY(itemOutline.getY() + itemOutline.getHeight());
				item.draw(itemOutline, s.fontMenu);
			}
		}
		
		// disable transparency again
		glDisable(GL_BLEND);
		
		// switch back to 3D rendering and restore transforms and such
		GUI.make3D();
	}

	/**
	 * Handle a mouse click event where the mouse is currently at the given
	 * position in window coordinates.
	 * 
	 * @param x X-coordinate of current mouse position.
	 * @param y Y-coordinate of current mouse position.
	 */
	public void handleMouseClickEvent(int x, int y) {
		// We cheat here: if the mouse has clicked at a menu item, then that
		// menu item must be hovered right now. Therefore, we do not need
		// cumbersome look-up functions now: we just click the hovered item
		// if there is any at all!
		for (MenuBarItem item : items) {
			if (item.isHovered()) {
				item.onClick();
				break;
			}
		}
	}
	
	/**
	 * Handle a mouse move event where the mouse is currently at the given
	 * position in window coordinates.
	 * 
	 * @param x X-coordinate of current mouse position.
	 * @param y Y-coordinate of current mouse position.
	 */
	public void handleMouseMoveEvent(int x, int y) {
		y = Display.getHeight() - y;
		
		// determine outline
		Rectangle outline = getOutline();
		
		// check if any (and if yes, which) menu item has been clicked
		if (position == Position.TOP || position == Position.BOTTOM) {
			// check if the vertical position matches at least
			if (y < outline.getY() - outline.getHeight() ||
					y > outline.getY()) {
				for (MenuBarItem item : items) {
					item.setHovered(false);
				}
				return;
			}
			
			// check if some horizontal position matches
			Rectangle itemOutline = new Rectangle();
			itemOutline.setX(outline.getX());
			itemOutline.setY(outline.getY());
			itemOutline.setHeight(outline.getHeight());
			itemOutline.setWidth(outline.getWidth() / items.size());
			for (MenuBarItem item : items) {
				if (itemOutline.getX() <= x && x <= itemOutline.getX() +
						itemOutline.getWidth())	{
					item.setHovered(true);
				} else {
					item.setHovered(false);
				}
				itemOutline.setX(itemOutline.getX() + itemOutline.getWidth());
			}
		} else {
			// check if the horizontal position matches at least
			if (x < outline.getX() ||
					x > outline.getX() + outline.getWidth()) {
				for (MenuBarItem item : items) {
					item.setHovered(false);
				}
				return;
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
				} else {
					item.setHovered(false);
				}
				itemOutline.setY(itemOutline.getY() + itemOutline.getHeight());
			}
		}
	}
	
	/**
	 * Handle a resize event of the display. That is, make sure that the
	 * menu bar is still positioned at the edge of the display.
	 * 
	 * @param width New width of the display.
	 * @param height New height of the display.
	 */
	public void handleResizeEvent(int width, int height) {
		this.outline = null;
		getOutline(width, height);
	}
	
	/**
	 * Assumes that it is called inside GL_QUADS. Will render a background
	 * on the correct spot, using the {@link Alignment} and {@link Position}
	 * of this menu bar.
	 */
	private void drawBackground(Rectangle outline) {
		glVertex2d(outline.getX(), outline.getY() - outline.getHeight());
		glVertex2d(outline.getX() + outline.getWidth(), outline.getY() - outline.getHeight());
		glVertex2d(outline.getX() + outline.getWidth(), outline.getY());
		glVertex2d(outline.getX(), outline.getY());
	}
	
	private Rectangle getOutline() {
		return getOutline(Display.getWidth(), Display.getHeight());
	}
	
	/**
	 * Return the outline of the menu bar. This makes sure that all items fit
	 * in the returned outline, which is positioned correctly with respect to
	 * the position and outline of the menu bar. Note that in case the position
	 * is either top or bottom, all items should be placed next to each other.
	 * Otherwise, all items should be placed below each other.
	 * 
	 * @param width The width of the display.
	 * @param height The height of the display.
	 * @return An easy-to-use outline.
	 */
	private Rectangle getOutline(int width, int height) {
		if (this.outline != null) {
			return this.outline;
		}
		
		Rectangle outline = new Rectangle();
		int itemHeight = state.fontMenu.getLineHeight() * 4 + 3 * MenuBarItem.PADDING;
		
		// determine maximal width of all items
		int maxWidth = 0, itemWidth;
		for (MenuBarItem item : items) {
			itemWidth = item.getPreferredWidth(state.fontMenu);
			if (itemWidth > maxWidth) {
				maxWidth = itemWidth;
			}
		}
		
		// determine width and height of outline
		if (position == Position.TOP || position == Position.BOTTOM) {
			// all items are next to each other
			outline.setHeight(itemHeight);
			outline.setWidth(items.size() * maxWidth);
		} else {
			// all items are below each other
			outline.setHeight(items.size() * itemHeight);
			outline.setWidth(maxWidth);
		}
		
		// determine position of rectangle
		switch (position) {
		case TOP :
		case BOTTOM :
			switch (alignment) {
			case BEGIN:
				outline.setX(MARGIN);
				break;
			case CENTER:
				outline.setX(MARGIN + (width - 2 * MARGIN -
						outline.getWidth()) / 2);
				break;
			case END:
				outline.setX(width - MARGIN - outline.getWidth());
				break;
			}
			
			if (position == Position.TOP) {
				outline.setY(MARGIN + outline.getHeight());
			} else {
				outline.setY(height - MARGIN);
			}
			break;
		case LEFT:
		case RIGHT:
			if (position == Position.LEFT) {
				outline.setX(MARGIN);
			} else {
				outline.setX(width - MARGIN - outline.getWidth());
			}
			
			switch (alignment) {
			case BEGIN:
				outline.setY(MARGIN + outline.getHeight());
				break;
			case CENTER:
				outline.setY(MARGIN + (height - 2 * MARGIN -
						outline.getHeight()) / 2 + outline.getHeight());
				break;
			case END:
				outline.setY(height - MARGIN);
				break;
			}
			break;
		}
		
		this.outline = outline;
		return outline;
	}
	
	/** State of the program. */
	private State state;
	/** Alignment of this menu bar. */
	private Alignment alignment;
	/** Position of this menu bar. */
	private Position position;
	/** Items in this menu bar. */
	private ArrayList<MenuBarItem> items;
	/** Outline of menu bar. If {@code null}, can be calculated using {@link #getOutline()}. */
	private Rectangle outline;
}
