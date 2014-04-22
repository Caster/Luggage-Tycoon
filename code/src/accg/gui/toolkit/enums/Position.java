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
		this.name = "Position." + name().toLowerCase();
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
	
	/**
	 * Returns the "opposite" position of the current one. For example, the
	 * opposite of TOP is BOTTOM. The opposite of LEFT is RIGHT.
	 * 
	 * @return The "opposite" position of the current one.
	 */
	public Position getOpposite() {
		switch (this) {
		case TOP: return BOTTOM;
		case RIGHT: return LEFT;
		case BOTTOM: return TOP;
		case LEFT: return RIGHT;
		}
		return null;
	}
	
	/**
	 * Returns if a component or container in this position should be laid out
	 * horizontally or not. This is true if and only if the position is
	 * {@link #BOTTOM} or {@link #TOP}.
	 * @return If a component or container in this position should be laid out
	 * horizontally or not.
	 */
	public boolean isHorizontal() {
		return (this == BOTTOM || this == TOP);
	}
	
	/**
	 * Returns if a component or container in this position should be laid out
	 * vertically or not. This is true if and only if the position is
	 * {@link #LEFT} or {@link #RIGHT}.
	 * @return If a component or container in this position should be laid out
	 * vertically or not.
	 */
	public boolean isVertical() {
		return (this == LEFT || this == RIGHT);
	}
}
