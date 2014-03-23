package accg.gui;

import org.newdawn.slick.opengl.Texture;

import accg.State;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.MenuBar;
import accg.gui.toolkit.Button;
import accg.gui.toolkit.Button.Type;
import accg.gui.toolkit.MenuStack;
import accg.gui.toolkit.MenuStack.Alignment;
import accg.gui.toolkit.event.MouseClickEvent;

/**
 * Menu bar containing the alignment settings.
 */
public class AlignmentMenuBar extends MenuBar {
	
	public AlignmentMenuBar(final MenuStack stack, final State s) {
		for (int i = 0; i < MenuStack.Alignment.values().length; i++) {
			add(generateAlignmentItem(i, stack, s));
		}
	}
	
	/**
	 * Generate a {@link Button} that represents a menu alignment.
	 * 
	 * @param index Index of the alignment to generate an item for.
	 * @param s State, used to look up icons in.
	 * @return A newly created {@link Button}.
	 */
	private Button generateAlignmentItem(final int index, final MenuStack stack, final State s) {
		final Alignment alignment = MenuStack.Alignment.values()[index];
		// create the item
		Button mbi = new Button(alignment.getName(),
				getAlignmentIcon(index, s), Type.CHECKABLE_UNIQUE);

		mbi.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.prefs.putInt("menu.alignment", index);
					stack.setAlignment(alignment);
				}
			}
		});
		
		// check the item if needed
		if (index == s.prefs.getInt("menu.alignment", State.DEF_MENU_ALIGNMENT)) {
			mbi.setChecked(true);
		}
		// return the result
		return mbi;
	}
	
	/**
	 * Return the icon that belongs to the {@link Alignment} with given ordinal index.
	 * 
	 * @param index Ordinal index of an {@link Alignment}.
	 * @param s State, used to look up icons in.
	 * @return An icon, or {@code null} if index is invalid.
	 */
	private Texture getAlignmentIcon(final int index, State s) {
		if (index < 0 || index >= MenuStack.Alignment.values().length) {
			return null;
		}
		
		switch (index) {
		case 0 :
			return s.textures.iconJustifyLeft;
		case 1 :
			return s.textures.iconJustifyCenter;
		case 2 :
			return s.textures.iconJustifyRight;
		default :
			return null;
		}
	}
}
