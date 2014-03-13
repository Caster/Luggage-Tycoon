package accg.objects;


/**
 * Abstract super class of all blocks.
 * 
 * A block has a position, which is indicated by three integers ({@link #x},
 * {@link #y} and {@link #z}).
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
	 * Possible orientations for a block.
	 */
	public static enum Orientation {
		
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
		
		public double angle;
		
		Orientation(double angle) {
			this.angle = angle;
		}
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
	}
	
	/**
	 * Return an exact copy of this block.
	 */
	public abstract Block clone();
	
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
	 * Returns the height of this block.
	 * @return The height.
	 */
	public abstract int getHeight();
	
	/**
	 * Returns the orientation of this block.
	 * @return The orientation.
	 */
	public Orientation getOrientation() {
		return orientation;
	}
	
	/**
	 * Change the x-coordinate of this block.
	 * @param x The new x-coordinate.
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Change the y-coordinate of this block.
	 * @param y The new y-coordinate.
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Change the z-coordinate of this block.
	 * @param z The new z-coordinate.
	 */
	public void setZ(int z) {
		this.z = z;
	}
}
