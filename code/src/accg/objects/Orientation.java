package accg.objects;

import javax.vecmath.Vector3f;

/**
 * Possible orientations for a block.
 */
public enum Orientation {
	
	/**
	 * This block is oriented towards the left (that is, towards the
	 * decreasing x-axis).
	 */
	LEFT(-90),
	
	/**
	 * This block is oriented upwards (that is, towards the
	 * increasing y-axis).
	 */
	UP(0),
	
	/**
	 * This block is oriented towards the right (that is, towards the
	 * increasing x-axis).
	 */
	RIGHT(90),
	
	/**
	 * This block is oriented downwards (that is, towards the
	 * decreasing y-axis).
	 */
	DOWN(180);
	
	/**
	 * The rotation angle that this {@link Orientation} represents.
	 */
	public double angle;
	
	Orientation(double angle) {
		this.angle = angle;
	}

	/**
	 * Parse an Orientation from a string. The given string is trimmed and after
	 * that a case-insensitive comparison is done. The following strings are
	 * currently supported:
	 * <ul>
	 *   <li>LEFT: "l" and "left".</li>
	 *   <li>UP: "u" and "up".</li>
	 *   <li>RIGHT: "r" and "right".</li>
	 *   <li>DOWN: "d" and "down".</li>
	 * </ul>
	 * 
	 * @param s String to parse.
	 * @return Parsed Orientation or {@code null} if the string could not be
	 *         parsed to a known Orientation.
	 */
	public static Orientation parseOrientation(String s) {
		switch (s.trim().toLowerCase()) {
		case "l":
		case "left":
			return LEFT;
		case "u":
		case "up":
			return UP;
		case "r":
		case "right":
			return RIGHT;
		case "d":
		case "down":
			return DOWN;
		}
		return null;
	}
	
	/**
	 * Returns the ID of an orientation, as can be used in a string
	 * representation or in a file, for example a saved game.
	 * @return The ID of an orientation. This is the first letter of the name,
	 *         in lower case. It is guaranteed to be unique.
	 */
	public String getOrientationID() {
		return Character.toString(Character.toLowerCase(name().charAt(0)));
	}
	
	/**
	 * Given a position in 3D, return a position next to this position on the
	 * same height that one would be in if doing one step in the direction
	 * represented by this Orientation.
	 * @param startPos Position to move from.
	 * @param distance Distance to move in given direction.
	 * @return Position where one would end up.
	 */
	public Vector3f moveFrom(Vector3f startPos, float distance) {
		float dx = (this == RIGHT ? distance : 0) + (this == LEFT ? -distance : 0);
		float dy = (this == UP ? distance : 0) + (this == DOWN ? -distance : 0);
		return new Vector3f(startPos.x + dx, startPos.y + dy, startPos.z);
	}
	
	/**
	 * Return the orientation you would obtain when turning left 90 degrees
	 * when in the current orientation.
	 * @return The rotated orientation.
	 */
	public Orientation rotateLeft() {
		return Orientation.values()[(ordinal() - 1 + values().length) %
		                            values().length];
	}
	
	/**
	 * Return the orientation you would obtain when turning right 90 degrees
	 * when in the current orientation.
	 * @return The rotated orientation.
	 */
	public Orientation rotateRight() {
		return Orientation.values()[(ordinal() + 1) % values().length];
	}
	
	/**
	 * Return the orientation you would obtain when turing 180 degrees when in
	 * the current orientation.
	 * @return The rotated orientation.
	 */
	public Orientation turnAround() {
		return Orientation.values()[(ordinal() + 2) % values().length];
	}
}
