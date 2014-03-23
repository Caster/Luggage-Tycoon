package accg.gui;

import org.newdawn.slick.opengl.Texture;

import accg.State;
import accg.gui.toolkit.*;
import accg.gui.toolkit.Button.Type;
import accg.gui.toolkit.MenuStack.Position;
import accg.gui.toolkit.event.MouseClickEvent;

/**
 * Menu bar containing the position settings.
 */
public class PositionMenuBar extends MenuBar {
	
	public PositionMenuBar(final MenuStack stack, final State s) {
		for (int i = 0; i < MenuStack.Position.values().length; i++) {
			add(generatePositionItem(i, stack, s));
		}
	}
	
	/**
	 * Generate a {@link Button} that represents a menu position.
	 * 
	 * @param index Index of the position to generate an item for.
	 * @param s State, used to look up icons in.
	 * @return A newly created {@link Button}.
	 */
	private Button generatePositionItem(final int index, final MenuStack stack, final State s) {
		final Position pos = MenuStack.Position.values()[index];
		// create the item
		Button mbi = new Button(pos.getName(),
				getPositionIcon(index, s), Type.CHECKABLE_UNIQUE);
		mbi.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.prefs.putInt("menu.position", index);
					stack.setPosition(pos);
				}
			}
		});
		
		// check the item if needed
		if (index == s.prefs.getInt("menu.position", State.DEF_MENU_POSITION)) {
			mbi.setChecked(true);
		}
		// return the result
		return mbi;
	}
	
	/**
	 * Return the icon that belongs to the {@link Position} with given ordinal index.
	 * 
	 * @param index Ordinal index of a {@link Position}.
	 * @param s State, used to look up icons in.
	 * @return An icon, or {@code null} if index is invalid.
	 */
	private Texture getPositionIcon(final int index, State s) {
		if (index < 0 || index >= MenuStack.Position.values().length) {
			return null;
		}
		
		switch (index) {
		case 0 :
			return s.textures.iconGoUp;
		case 1 :
			return s.textures.iconGoRight;
		case 2 :
			return s.textures.iconGoDown;
		case 3 :
			return s.textures.iconGoLeft;
		default :
			return null;
		}
	}
}
