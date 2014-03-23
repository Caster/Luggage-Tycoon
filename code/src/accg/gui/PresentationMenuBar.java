package accg.gui;

import org.newdawn.slick.opengl.Texture;

import accg.State;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.MenuBar;
import accg.gui.toolkit.MenuBarItem;
import accg.gui.toolkit.MenuBarItem.Presentation;
import accg.gui.toolkit.MenuBarItem.Type;
import accg.gui.toolkit.MenuStack;
import accg.gui.toolkit.event.MouseClickEvent;

/**
 * Menu bar containing the presentation settings.
 */
public class PresentationMenuBar extends MenuBar {
	
	public PresentationMenuBar(final MenuStack stack, final State s) {
		for (int i = 0; i < MenuBarItem.Presentation.values().length; i++) {
			add(generatePresentationItem(i, stack, s));
		}
	}
	/**
	 * Generate a {@link MenuBarItem} that represents a menu presentation.
	 * 
	 * @param index Index of the presentation to generate an item for.
	 * @param s State, used to look up icons in.
	 * @return A newly created {@link MenuBarItem}.
	 */
	private MenuBarItem generatePresentationItem(final int index, final MenuStack stack, final State s) {
		final Presentation pres = MenuBarItem.Presentation.values()[index];
		// create the item
		MenuBarItem mbi = new MenuBarItem(pres.getName(),
				getPresentationIcon(index, s), Type.CHECKABLE_UNIQUE);
		mbi.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.prefs.putInt("menu.presentation", index);
					stack.setPresentation(pres);
				}
			}
		});
		// check the item if needed
		if (index == s.prefs.getInt("menu.presentation", State.DEF_MENU_PRESENTATION)) {
			mbi.setChecked(true);
		}
		// return the result
		return mbi;
	}
	
	/**
	 * Return the icon that belongs to the {@link Presentation} with given ordinal index.
	 * 
	 * @param index Ordinal index of a {@link Presentation}.
	 * @param s State, used to look up icons in.
	 * @return An icon, or {@code null} if index is invalid.
	 */
	private Texture getPresentationIcon(final int index, State s) {
		if (index < 0 || index >= MenuBarItem.Presentation.values().length) {
			return null;
		}
		
		switch (index) {
		case 0 :
			return s.textures.iconZoomOut;
		case 1 :
			return s.textures.iconZoomIn;
		default :
			return null;
		}
	}
}
