package accg.gui.toolkit.enums;

import accg.gui.toolkit.containers.MenuBar;

/**
 * Possible presentations/layouts for items in a {@link MenuBar}. A
 * presentation defines the style of button.
 */
public enum Presentation {
	
	/**
	 * A small icon is shown on the left of the text.
	 */
	ICON_LEFT_TEXT("Presentation.iconLeft"),
	
	/**
	 * A large icon is shown on top of the text.
	 */
	ICON_ABOVE_TEXT("Presentation.iconLarge");
	
	private Presentation(String name) {
		this.name = name;
	}
	
	private String name;
	
	/**
	 * Return a human-readable description of the presentation type.
	 * 
	 * @return A human-readable, short description.
	 */
	public String getName() {
		return name;
	}
}
