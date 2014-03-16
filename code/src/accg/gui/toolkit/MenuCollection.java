package accg.gui.toolkit;

import static org.lwjgl.opengl.GL11.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.Rectangle;

/**
 * This class manages a GUI with several possible menu bars.
 */
public class MenuCollection extends Container {

	/**
	 * Distance between edge of screen and the menu bar.
	 * 
	 * In case this is a submenu, the menu bar is drawn directly next
	 * to its parent.
	 */
	public static final int MARGIN = 10;
	
	/**
	 * The menu bars, with associated keys.
	 */
	private Map<Object, MenuBar> menuBars;
	
	/**
	 * The menu bar that is currently visible. If <code>null</code>, no menu is
	 * visible at the moment.
	 */
	private MenuBar visibleMenu;
	
	/**
	 * Alignment of this menu collection.
	 */
	private Alignment alignment;
	
	/**
	 * Possible alignments for items inside the {@link MenuBar}.
	 * 
	 * Picking an alignment will move the menu bar to the according position
	 * on the screen.
	 */
	public enum Alignment {
		/** Left or top. */
		BEGIN("Left / top"),
		/** Center. */
		CENTER("Center"),
		/** Right or bottom. */
		END("Right / bottom");
		
		private Alignment(String name) {
			this.name = name;
		}
		
		private String name;
		
		/**
		 * Returns a human-readable description of the alignment type.
		 * 
		 * @return A human-readable, short description.
		 */
		public String getName() {
			return name;
		}
	}
	
	/**
	 * Position of this menu collection.
	 */
	private Position position;
	
	/**
	 * Possible positions for a {@link MenuBar}.
	 */
	public enum Position {
		TOP, RIGHT, BOTTOM, LEFT;
		
		private Position() {
			this.name = name().charAt(0) + name().substring(1).toLowerCase();
		}
		
		private Position(String name) {
			this.name = name;
		}
		
		private String name;
		
		/**
		 * Returns a human-readable description of the position type.
		 * 
		 * @return A human-readable, short description.
		 */
		public String getName() {
			return name;
		}
	}
	
	/**
	 * Creates a new menu collection, with no menu bars.
	 */
	public MenuCollection() {
		menuBars = new HashMap<>();
		
		alignment = Alignment.CENTER;
		position = Position.TOP;
	}
	
	@Override
	public int getPreferredWidth() {
		return visibleMenu.getPreferredWidth();
	}
	
	@Override
	public int getPreferredHeight() {
		return visibleMenu.getPreferredHeight();
	}

	@Override
	public void layout() {
		
		visibleMenu.layout();
		
		// first compute and set the width and height of the menu
		if (visibleMenu.getPreferredWidth() < getWidth() - 2 * MARGIN) {
			visibleMenu.setWidth(visibleMenu.getPreferredWidth());
		} else {
			visibleMenu.setWidth(getWidth() - 2 * MARGIN);
		}
		
		if (visibleMenu.getPreferredHeight() < getHeight() - 2 * MARGIN) {
			visibleMenu.setHeight(visibleMenu.getPreferredHeight());
		} else {
			visibleMenu.setHeight(getHeight() - 2 * MARGIN);
		}
		
		// determine position of the menu, based on the width and height
		switch (position) {
		case TOP:
		case BOTTOM:
			switch (getAlignment()) {
			case BEGIN:
				visibleMenu.setX(MARGIN);
				break;
			case END:
				visibleMenu.setX(getWidth() - MARGIN - visibleMenu.getWidth());
				break;
			default: // CENTER or null
				visibleMenu.setX(MARGIN + (getWidth() - 2 * MARGIN -
						visibleMenu.getWidth()) / 2);
				break;
			}
			
			if (position == Position.TOP) {
				visibleMenu.setY(MARGIN);
			} else {
				visibleMenu.setY(getHeight() - MARGIN - visibleMenu.getHeight());
			}
			break;
		default: // LEFT, RIGHT or null
			if (position == Position.LEFT) {
				visibleMenu.setX(MARGIN);
			} else {
				visibleMenu.setX(getWidth() - MARGIN - visibleMenu.getWidth());
			}
			
			switch (getAlignment()) {
			case BEGIN:
				visibleMenu.setY(MARGIN);
				break;
			case END:
				visibleMenu.setY(getHeight() - visibleMenu.getHeight() - MARGIN);
				break;
			default: // CENTER or null
				visibleMenu.setY(MARGIN + (visibleMenu.getHeight() - 2 * MARGIN) / 2);
				break;
			}
			break;
		}
	}
	
	@Override
	public String getComponentName() {
		return "MenuCollection";
	}
	
	@Override
	public void add(Component toAdd) {
		throw new UnsupportedOperationException("Adding to a MenuCollection is not supported; see addMenuBar() instead");
	}

	@Override
	protected boolean isTransparent() {
		return true;
	}
	
	/**
	 * Adds a new menu bar.
	 * 
	 * @param key A key. This can be any object; you can use this to retrieve
	 * the menu bar later.
	 * @param menuBar The menu bar to add.
	 * @throws NullPointerException If <code>menuBar == null</code>.
	 */
	public void addMenuBar(Object key, MenuBar menuBar) {
		if (menuBar == null) {
			throw new NullPointerException("Cannot add null menu bar to collection");
		}
		
		menuBar.setParent(this);
		menuBars.put(key, menuBar);
	}
	
	/**
	 * Sets the currently visible menu bar.
	 * 
	 * @param key The key of the menu bar to show, as defined when calling
	 * {@link #addMenuBar(Object, MenuBar)}.
	 * @throws IllegalArgumentException If there is no menu with the given key.
	 */
	public void setMenuBar(Object key) {
		MenuBar newMenu = menuBars.get(key);
		
		if (newMenu == null) {
			throw new IllegalArgumentException("Menu collection does not "
					+ "contain a menu with key [" + key + "]");
		}
		
		for (MenuBar m : menuBars.values()) {
			m.setVisible(false);
		}
		newMenu.setVisible(true);
		
		visibleMenu = newMenu;
	}
	
	/**
	 * Removes the menu bar with the given key. If this menu bar was currently
	 * visible, no menu will be visible anymore afterwards (as if
	 * {@link #hideMenuBar()} was called).
	 * 
	 * @param key The key of the menu bar to remove, as defined when calling
	 * {@link #addMenuBar(Object, MenuBar)}.
	 * @throws IllegalArgumentException If there is no menu with the given key.
	 */
	public void removeMenuBar(Object key) {
		MenuBar menuToRemove = menuBars.get(key);
		
		if (menuToRemove == null) {
			throw new IllegalArgumentException("Menu collection does not "
					+ "contain a menu with key [" + key + "]");
		}
		
		if (visibleMenu == menuToRemove) {
			hideMenuBar();
		}
		
		menuBars.remove(key);
	}
	
	/**
	 * Hides the menu bar, so that no menu bar is visible anymore.
	 * 
	 * To show the menu bar again, use {@link #setMenuBar(Object)}.
	 */
	public void hideMenuBar() {
		visibleMenu = null;
	}
	
	/**
	 * Changes the alignment of this menu collection.
	 * @param alignment New alignment for this menu collection.
	 */
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	/**
	 * Returns the alignment of this menu collection.
	 * @return The alignment.
	 */
	public Alignment getAlignment() {
		return alignment;
	}
	
	/**
	 * Changes the position of this menu collection.
	 * @param position New position for this menu collection.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * Returns the position of this menu collection.
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public Collection<? extends Component> getChildren() {
		return menuBars.values();
	}
}
