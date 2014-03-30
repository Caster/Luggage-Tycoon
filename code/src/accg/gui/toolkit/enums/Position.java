package accg.gui.toolkit.enums;

import accg.gui.toolkit.containers.MenuStack;

/**
 * Possible positions for a {@link MenuStack}.
 */
public enum Position {
	
	/**
	 * The menu stack is shown on the top of the screen.
	 */
	TOP,

	/**
	 * The menu stack is shown on the right of the screen.
	 */
	RIGHT,

	/**
	 * The menu stack is shown on the bottom of the screen.
	 */
	BOTTOM,

	/**
	 * The menu stack is shown on the left of the screen.
	 */
	LEFT;
	
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
