package accg.gui.toolkit.enums;

import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.containers.MenuBar;

/**
 * The ButtonType of a {@link Button} indicates what properties it
 * has. Refer to the documentation of the enum members for details.
 */
public enum ButtonType {
	
	/**
	 * A regular button.
	 */
	NORMAL,
	
	/**
	 * A button that has a boolean isChecked property. When
	 * clicked, the value of that property is toggled and this is
	 * also indicated visually.
	 */
	CHECKABLE,
	
	/**
	 * Same as a {@link #CHECKABLE} menu item, but whenever a button
	 * is checked, all other buttons in the same {@link MenuBar}
	 * are automatically unchecked.
	 * 
	 * <p>This is of course only applicable if the Button is actually
	 * put in a {@link MenuBar} and functions as a menu item.
	 */
	CHECKABLE_UNIQUE
}
