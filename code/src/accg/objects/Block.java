package accg.objects;

import accg.objects.blocks.AscendingConveyorBlock;
import accg.objects.blocks.BendLeftConveyorBlock;
import accg.objects.blocks.BendRightConveyorBlock;
import accg.objects.blocks.DescendingConveyorBlock;
import accg.objects.blocks.EnterBlock;
import accg.objects.blocks.FlatConveyorBlock;

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
	 * If this block can be deleted by the user or not. Can be used to add
	 * blocks to a scene that cannot be modified by a user, e.g. a block where
	 * luggage enters or leaves the scene for example.
	 */
	protected boolean deletable;
	
	/**
	 * Factor that may be used when drawing the block.
	 */
	protected float scaleFactor;
	
	/**
	 * Creates a new block on the specified position with the given orientation,
	 * which is deletable (it can may removed by the user).
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
		this.deletable = true;
		this.scaleFactor = 1;
	}
	
	/**
	 * Creates a new block on the specified position with the given orientation,
	 * which is deletable if specified.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 * @param deletable If this block may be deleted by a user or not.
	 */
	public Block(int x, int y, int z, Orientation orientation,
			boolean deletable) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.orientation = orientation;
		this.deletable = deletable;
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
	 * Returns if this block may be deleted by the user or not.
	 * @return If this block may be deleted by the user or not.
	 */
	public boolean isDeletable() {
		return deletable;
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

	/**
	 * Construct an appropriate block and return that. May return {@code null}
	 * if the given {@code blockID} is unknown.
	 * 
	 * @param blockID ID of a block. For example, "eb" maps to "EnterBlock".
	 * @param x X-coordinate of block to construct.
	 * @param y Y-coordinate of block to construct.
	 * @param z Z-coordinate of block to construct.
	 * @param orientation Orientation of block to construct.
	 * @param deletable If the block to construct is deletable or not.
	 * @return An appropriate block, or {@code null}.
	 */
	public static Block getBlock(String blockID, int x, int y,
			int z, Orientation orientation, boolean deletable) {
		switch (blockID) {
		case "eb":
			return new EnterBlock(x, y, z, orientation,
					EnterBlock.DEFAULT_TIME_BETWEEN_LUGGAGE, deletable);
		case "lb":
			// TODO: Implement LeaveBlock.
		case "cf":
			return new FlatConveyorBlock(x, y, z, orientation, deletable);
		case "ca":
			return new AscendingConveyorBlock(x, y, z, orientation, deletable);
		case "cd":
			return new DescendingConveyorBlock(x, y, z, orientation, deletable);
		case "cbl":
			return new BendLeftConveyorBlock(x, y, z, orientation, deletable);
		case "cbr":
			return new BendRightConveyorBlock(x, y, z, orientation, deletable);
		}
		return null;
	}
}
