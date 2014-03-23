package accg.objects;

import javax.vecmath.Vector3f;



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
	 * Factor that may be used when drawing the block.
	 */
	protected float scaleFactor;
	
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
		
		/**
		 * The rotation angle that this {@link Orientation} represents.
		 */
		public double angle;
		
		Orientation(double angle) {
			this.angle = angle;
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
		this.scaleFactor = 1;
	}
	
	/**
	 * Returns an exact copy of this block.
	 */
	@Override
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
	 * Returns the scale factor of this block.
	 * @return The scale factor.
	 */
	public double getScaleFactor() {
		return scaleFactor;
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
	
	/**
	 * Change the orientation of this block.
	 * @param orientation The new orientation.
	 */
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	
	/**
	 * Change the scale factor of this block.
	 * @param scaleFactor The new scale factor.
	 */
	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
}
