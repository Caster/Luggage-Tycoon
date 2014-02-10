package accg.objects;

import accg.State;

/**
 * Abstract super class of all blocks.
 * 
 * A block has a position, which is indicated by three integers ({@link #x},
 * {@link #y} and {@link #z}).
 * 
 * The {@link #draw(State)} method of a {@link Block} may also draw
 * other blocks. Therefore, {@link Block} contains a variable
 * {@link #alreadyDrawn} that indicates whether the block has already been
 * drawn, either by its own {@link #draw(State)} method, or by another
 * block's {@link #draw(State)} method. This prevents blocks from being drawn
 * multiple times.
 */
public abstract class Block extends DrawableObject {
	
	/**
	 * The x-coordinate of this block.
	 */
	protected int x;
	
	/**
	 * The y-coordinate of this block.
	 */
	protected int y;
	
	/**
	 * The z-coordinate of this block.
	 */
	protected int z;
	
	/**
	 * The orientation of this block.
	 */
	protected Orientation orientation;
	
	/**
	 * Whether this block is already drawn.
	 */
	protected boolean alreadyDrawn;
	
	/**
	 * Possible orientations for a block.
	 */
	public static enum Orientation {
		
		/**
		 * This block is oriented towards the left (that is, towards the
		 * decreasing x-axis).
		 */
		LEFT,
		
		/**
		 * This block is oriented upwards (that is, towards the
		 * increasing y-axis).
		 */
		UP,
		
		/**
		 * This block is oriented towards the right (that is, towards the
		 * increasing x-axis).
		 */
		RIGHT,
		
		/**
		 * This block is oriented downwards (that is, towards the
		 * decreasing y-axis).
		 */
		DOWN,
	}
	
	/**
	 * Creates a new block on the specified position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 */
	public Block(int x, int y, int z, Orientation orientation) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.orientation = orientation;
		
		this.alreadyDrawn = false;
	}
	
	/**
	 * Returns whether this block is already drawn.
	 * @return Whether this block is already drawn.
	 */
	public boolean isAlreadyDrawn() {
		return alreadyDrawn;
	}

	/**
	 * Sets whether this block is already drawn.
	 * @param alreadyDrawn Whether this block is already drawn.
	 */
	public void setAlreadyDrawn(boolean alreadyDrawn) {
		this.alreadyDrawn = alreadyDrawn;
	}
	
	/**
	 * Returns the x-coordinate of this block.
	 * @return The x-coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y-coordinate of this block.
	 * @return The y-coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns the z-coordinate of this block.
	 * @return The z-coordinate.
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Returns the orientation of this block.
	 * @return The orientation.
	 */
	public Orientation getOrientation() {
		return orientation;
	}
}
