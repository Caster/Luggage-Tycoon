package accg.gui.toolkit.containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accg.gui.toolkit.Component;
import accg.gui.toolkit.Container;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.enums.Alignment;
import accg.gui.toolkit.enums.Orientation;
import accg.gui.toolkit.enums.Position;
import accg.gui.toolkit.enums.Presentation;

/**
 * This class manages a GUI with several possible menu bars.
 * 
 * The GUI consists of several menu bars, that are associated with keys to refer
 * to them. Then, one or more of these menu bars can be shown on the screen in a
 * stack of menu bars. Normally, the first one in the stack will be the one that
 * is shown first and from there other menu bars can be opened (so those are sub
 * menu bars).
 * 
 * In the {@link Listener} of a {@link Button}, one can for example use the
 * following code to open a new sub menu:
 * <pre>
 *     addMenuBelowOrClose(menuThisButtonIsIn, menuToAdd);
 * </pre>
 * In this case the sub menu that was already there (and other sub menus of that
 * sub menu) are hidden automatically and replaced by <code>menuToAdd</code>. To
 * close all sub menus below the given menu, you can use:
 * <pre>
 *     removeAllBelow(menuThisButtonIsIn);
 * </pre>
 */
public class MenuStack extends Container {

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
	protected Map<Object, MenuBar> menuBars;
	
	/**
	 * The menu bars that are currently visible.
	 * 
	 * The first element in this list is the one that is shown closest to the
	 * screen edge.
	 */
	protected ArrayList<MenuBar> visibleMenus;
	
	/**
	 * Alignment of this menu collection.
	 */
	protected Alignment alignment;
	
	/**
	 * Position of this menu collection.
	 */
	protected Position position;
	
	/**
	 * The presentation of all buttons in this menu stack.
	 * 
	 * The {@link MenuStack} will set this for all visible menus.
	 */
	protected Presentation presentation;
	
	/**
	 * Creates a new menu collection, with no menu bars.
	 */
	public MenuStack() {
		menuBars = new HashMap<>();
		visibleMenus = new ArrayList<>();
		
		alignment = Alignment.CENTER;
		position = Position.TOP;
		presentation = Presentation.ICON_ABOVE_TEXT;
	}
	
	@Override
	public int getPreferredWidth() {
		// take the maximum preferred width of all visible menus
		int width = 0;
		
		for (MenuBar m : visibleMenus) {
			int mWidth = m.getPreferredWidth();
			if (mWidth > width) {
				width = mWidth;
			}
		}
		
		return width;
	}
	
	@Override
	public int getPreferredHeight() {
		return 0; // TODO implement this (although it is not used...)
	}

	@Override
	public void layout() {
		
		// we iterate over all menus to position them one by one
		int coord = 0;
		
		for (MenuBar m : visibleMenus) {
			coord = layoutMenuBar(m, coord);
		}
	}
	
	/**
	 * Layouts a single menu bar. This is called from {@link #layout()}
	 * for every menu bar.
	 * 
	 * Override this if you want to change the layout behaviour; alternatively,
	 * you may override {@link #layout()}.
	 * 
	 * @param m The menu bar to layout.
	 * @param coord The bottom (or right) coordinate of the previous menu bar. This is
	 * used to stack them.
	 * @return The bottom (or right) coordinate of the newly layouted menu bar. Tip:
	 * to stack menu bars, use this as the <code>coord</code> parameter for the next
	 * invocation of this method :)
	 */
	protected int layoutMenuBar(MenuBar m, int coord) {

		// layout all visible menus first
		m.layoutIfNeeded();
		
		// first compute and set the width and height of the menu
		if (m.getPreferredWidth() < getWidth() - 2 * MARGIN) {
			m.setWidth(m.getPreferredWidth());
		} else {
			m.setWidth(getWidth() - 2 * MARGIN);
		}
		
		if (m.getPreferredHeight() < getHeight() - 2 * MARGIN) {
			m.setHeight(m.getPreferredHeight());
		} else {
			m.setHeight(getHeight() - 2 * MARGIN);
		}
		
		// determine position of the menu, based on the width and height
		switch (position) {
		case TOP:
		case BOTTOM:
			switch (getAlignment()) {
			case BEGIN:
				m.setX(MARGIN);
				break;
			case END:
				m.setX(getWidth() - MARGIN - m.getWidth());
				break;
			default: // CENTER or null
				m.setX((getWidth() - m.getWidth()) / 2);
				break;
			}
			
			if (position == Position.TOP) {
				m.setY(coord + MARGIN);
			} else {
				m.setY(getHeight() - coord - MARGIN - m.getHeight());
			}
			break;
		default: // LEFT, RIGHT or null
			if (position == Position.LEFT) {
				m.setX(coord + MARGIN);
			} else {
				m.setX(getWidth() - coord - MARGIN - m.getWidth());
			}
			
			switch (getAlignment()) {
			case BEGIN:
				m.setY(MARGIN);
				break;
			case END:
				m.setY(getHeight() - m.getHeight() - MARGIN);
				break;
			default: // CENTER or null
				m.setY((getHeight() - m.getHeight()) / 2);
				break;
			}
			break;
		}
		
		return coord + MARGIN +
				((position == Position.TOP || position == Position.BOTTOM) ?
						m.getHeight() :
						m.getWidth()
				);
	}
	
	@Override
	public String getComponentName() {
		return "MenuStack";
	}
	
	@Override
	public void add(Component toAdd) {
		throw new UnsupportedOperationException("Adding to a MenuStack is not supported; see addMenuBar() instead");
	}

	@Override
	protected boolean isTransparent() {
		return true;
	}
	
	/**
	 * Adds a new menu bar to the collection.
	 * 
	 * @param key A key. This can be any object; you can use this to retrieve
	 * the menu bar later.
	 * @param menuBar The menu bar to add.
	 * @throws NullPointerException If <code>menuBar == null</code>.
	 */
	public void addToCollection(Object key, MenuBar menuBar) {
		if (menuBar == null) {
			throw new NullPointerException("Cannot add null menu bar to collection");
		}
		
		menuBar.setParent(this);
		menuBars.put(key, menuBar);
		
		updateVisibilities();
	}

	/**
	 * Adds a new menu bar below the <code>parent</code>, except if the same
	 * menu bar is already shown there: in that case this is removed.
	 * 
	 * This is the behaviour you usually want from toggle buttons that show
	 * a sub menu when selected. If you do not want the closing behaviour,
	 * use {@link #addMenuBelow(MenuBar, Object)}.
	 * 
	 * @param origin The menu bar to put the new sub menu below.
	 * @param key The key of the sub menu to add.
	 * @throws IllegalArgumentException If <code>parent</code> is not
	 * <code>null</code> and it is not visible.
	 */
	public void addMenuBelowOrClose(MenuBar origin, Object key) {
		if (origin == null) {
			addMenuOnPosition(0, menuBars.get(key));
		}
		
		int index = visibleMenus.indexOf(origin);
		if (index == -1) {
			throw new IllegalArgumentException("Origin menu is not visible");
		}
		
		// if we have the same menu already, close it
		if (visibleMenus.size() > index + 1
				&& visibleMenus.get(index + 1) == menuBars.get(key)) {
			removeAllBelow(index + 1);
		} else {
			addMenuOnPosition(index + 1, key);
		}
	}
	
	/**
	 * Adds a new menu bar below the <code>parent</code>.
	 * 
	 * @param origin The menu bar to put the new sub menu below.
	 * @param key The key of the sub menu to add.
	 * @throws IllegalArgumentException If <code>parent</code> is not
	 * <code>null</code> and it is not visible.
	 */
	public void addMenuBelow(MenuBar origin, Object key) {
		if (origin == null) {
			addMenuOnPosition(0, key);
		}
		
		int index = visibleMenus.indexOf(origin);
		if (index == -1) {
			throw new IllegalArgumentException("Origin menu is not visible");
		}

		addMenuOnPosition(index + 1, key);
	}
	
	/**
	 * Adds a new menu bar at the given index. The menu bar that currently may be
	 * on that index, and all menu bars below that, are removed.
	 * 
	 * If the menu bar to add already is on this index, nothing is done (that means,
	 * also sub-menu bars will not be removed).
	 * 
	 * @param index The index to put the new menu bar on.
	 * @param key The key of the sub menu to add.
	 */
	public void addMenuOnPosition(int index, Object key) {
		
		MenuBar newBar = menuBars.get(key);
		
		if (visibleMenus.size() > index && newBar == visibleMenus.get(index)) {
			return;
		}
		
		removeAllBelow(index);
		visibleMenus.add(newBar);
		
		updateVisibilities();
		
		needsLayout();
	}
	
	/**
	 * Removes all menu bars at and below the given menu bar.
	 * 
	 * @param origin The highest menu bar to remove.
	 */
	public void removeAllBelow(MenuBar origin) {
		int index = visibleMenus.indexOf(origin);
		if (index == -1) {
			throw new IllegalArgumentException("Origin menu is not visible");
		}
		removeAllBelow(index);
	}
	
	/**
	 * Removes all menu bars at and below the given index.
	 * 
	 * @param index The index of the highest menu bar to remove.
	 */
	public void removeAllBelow(int index) {
		while (visibleMenus.size() > index) {
			visibleMenus.remove(visibleMenus.get(visibleMenus.size() - 1));
		}
		
		updateVisibilities();
		
		needsLayout();
	}
	
	/**
	 * For every menu in the collection, update its visibility property based
	 * on whether it should be visible in the stack.
	 * 
	 * This method should be called after every modification of the stack or
	 * collection.
	 * 
	 * TODO An alternative approach would of course be to call
	 * {@link #setVisible(boolean)} immediately when needed.
	 */
	protected void updateVisibilities() {
		
		for (MenuBar m : menuBars.values()) {
			m.setVisible(false);
		}
		
		for (MenuBar m : visibleMenus) {
			m.setVisible(true);
		}
	}
	
	/**
	 * Changes the alignment of this menu stack.
	 * @param alignment New alignment for this menu stack.
	 */
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	/**
	 * Returns the alignment of this menu stack.
	 * @return The alignment.
	 */
	public Alignment getAlignment() {
		return alignment;
	}
	
	/**
	 * Changes the position of this menu stack.
	 * @param position New position for this menu stack.
	 */
	public void setPosition(Position position) {
		this.position = position;
		
		updateOrientations();
	}

	/**
	 * Returns the position of this menu collection.
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Changes the presentation of all buttons in this menu stack.
	 * @param presentation New presentation for this menu stack.
	 */
	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
		
		updatePresentations();
	}

	/**
	 * For every menu in the collection, update its orientation property based
	 * on the {@link Position} of this menu stack.
	 */
	protected void updateOrientations() {
		if (position == Position.TOP || position == Position.BOTTOM) {
			for (MenuBar m : menuBars.values()) {
				m.setOrientation(Orientation.HORIZONTAL);
			}
		} else {
			for (MenuBar m : menuBars.values()) {
				m.setOrientation(Orientation.VERTICAL);
			}
		}
	}
	
	/**
	 * Sets all buttons in all menu bars to the set presentation.
	 */
	protected void updatePresentations() {
		for (MenuBar m : menuBars.values()) {
			for (Button c : m.items) {
				c.setPresentation(presentation);
			}
		}
	}

	@Override
	public java.util.List<? extends Component> getChildren() {
		return new ArrayList<>(menuBars.values());
	}
}
