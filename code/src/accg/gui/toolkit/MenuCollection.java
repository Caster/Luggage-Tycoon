package accg.gui.toolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages a GUI with several possible menu bars.
 */
public class MenuCollection extends Component {
	
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
	
	@Override
	public void draw() {
		if (visibleMenu != null) {
			visibleMenu.draw();
		}
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
	public int getPreferredWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPreferredHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
}
