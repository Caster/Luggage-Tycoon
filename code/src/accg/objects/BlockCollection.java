package accg.objects;

import java.util.Iterator;
import java.util.NoSuchElementException;

import accg.State;
import accg.objects.blocks.ConveyorBlock;

/**
 * The collection of blocks to draw.
 * 
 * In this class, all z-coordinates are considered to be multiplied by
 * 4.
 * 
 * \todo This class at the moment is a mixture of model and view code.
 * It would be nice to split this into two classes, where this class only
 * becomes responsible for drawing the contents of the model class.
 */
public class BlockCollection extends DrawableObject implements Iterable<Block> {
	
	/**
	 * Array of blocks. <code>blocks[x][y][z]</code> contains the block
	 * on the coordinate (x, y, z / 4), or <code>null</code> if there is no
	 * block there.
	 */
	private Block[][][] blocks;
	
	/**
	 * The X-size of this {@link BlockCollection}, in the number of blocks.
	 */
	private int sizeX;
	
	/**
	 * The Y-size of this {@link BlockCollection}, in the number of blocks.
	 */
	private int sizeY;
	
	/**
	 * The Z-size of this {@link BlockCollection}, in the number of blocks.
	 */
	private int sizeZ;
	
	/**
	 * Creates a new {@link BlockCollection} of the given size.
	 * 
	 * @param sizeX The size in the x-direction.
	 * @param sizeY The size in the y-direction.
	 * @param sizeZ The size in the z-direction.
	 */
	public BlockCollection(int sizeX, int sizeY, int sizeZ) {
		this.blocks = new Block[sizeX][sizeY][sizeZ];
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
	}
	
	/**
	 * Sets the block on a position given by that block.
	 * 
	 * @param block The new block to be put in this collection.
	 * @throws NullPointerException If <code>block == null</code>.
	 * @see #setBlock(Block, int, int, int)
	 */
	public void setBlock(Block block) {
		setBlock(block, block.x, block.y, block.z);
	}
	
	/**
	 * Sets the block on a given position.
	 * 
	 * @param block The new block to be put on the given position.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @throws NullPointerException If <code>block == null</code>.
	 */
	public void setBlock(Block block, int x, int y, int z) {
		if (block == null) {
			throw new NullPointerException("Drawn blocks cannot be null");
		}
		blocks[x][y][z] = block;
	}

	/**
	 * Removes the block from the given position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @throws IllegalArgumentException If there was no block on the given
	 * position.
	 */
	public void removeBlock(int x, int y, int z) {
		if (blocks[x][y][z] == null) {
			throw new IllegalArgumentException("There was no block on coordinate (" +
							x + ", " + y + ", " + z + ")");
		}
		blocks[x][y][z] = null;
	}

	/**
	 * Returns whether the given coordinate is within the bounds of the world.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate (divided by 4).
	 * @return The block on coordinate (x, y, z / 4), or <code>null</code> if
	 * there is no block there.
	 */
	public boolean inBounds(int x, int y, int z) {
		return x >= 0 && x < sizeX
		    && y >= 0 && y < sizeY
		    && z >= 0 && z < sizeZ;
	}
	
	/**
	 * Returns the block that is exactly on the given coordinate. This method will
	 * return a block only if it is defined to be exactly on that coordinate.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate (divided by 4).
	 * @return The block on coordinate (x, y, z / 4), or <code>null</code> if
	 * there is no block there.
	 * @throws ArrayIndexOutOfBoundsException If {@link #inBounds(int, int, int)}
	 * returns <code>false</code> for this coordinate.
	 */
	public Block getBlock(int x, int y, int z) {
		return blocks[x][y][z];
	}
	
	/**
	 * Returns the block that is exactly on the given coordinate, or extends
	 * to the given coordinate.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate (divided by 4).
	 * @return The block on coordinate (x, y, z / 4), or <code>null</code> if
	 * there is no block there.
	 * @throws IllegalArgumentException If {@link #inBounds(int, int, int)}
	 * returns <code>false</code> for this coordinate.
	 */
	public Block getBlockFuzzy(int x, int y, int z) {
		
		if (!inBounds(x, y, z)) {
			throw new IllegalArgumentException("Block outside bounds");
		}
		
		for (int z2 = z; z2 >= 0; z2--) {
			Block block = blocks[x][y][z2];
			
			if (block != null && z2 + block.getHeight() > z) {
				return block;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns if it would be possible to place a block with the given height at
	 * the given position. This checks if there are no blocks in the way.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate (divided by 4).
	 * @param height Height of the block to check.
	 * @return If a block with given height could be placed at the given position.
	 * @throws ArrayIndexOutOfBoundsException If {@link #inBounds(int, int, int)}
	 * returns <code>false</code> for this coordinate.
	 */
	public boolean checkBlockFuzzy(int x, int y, int z, int height) {
		for (int z2 = z + height - 1; z2 >= z; z2--) {
			if (!inBounds(x, y, z2) || blocks[x][y][z2] != null) {
				return false;
			}
		}
		
		return (getBlockFuzzy(x, y, z) == null);
	}
	
	/**
	 * Returns the X-size of this {@link BlockCollection}.
	 * @return The size.
	 */
	public int getSizeX() {
		return sizeX;
	}
	
	/**
	 * Returns the Y-size of this {@link BlockCollection}.
	 * @return The size.
	 */
	public int getSizeY() {
		return sizeY;
	}
	
	/**
	 * Returns the Z-size of this {@link BlockCollection}.
	 * @return The size.
	 */
	public int getSizeZ() {
		return sizeZ;
	}
	
	@Override
	public void draw(State s) {
		
		for (Block[][] blockX : blocks) {
			for (Block[] blockXY : blockX) {
				for (Block block : blockXY) {
					if (block != null) {
						block.draw(s);
					}
				}
			}
		}
		
		// finally, ConveyorBlocks get a special treatment:
		// their arrow must be drawn and due to OpenGL blending limitations,
		// that can only be done after all other blocks are drawn
		for (Block[][] blockX : blocks) {
			for (Block[] blockXY : blockX) {
				for (Block block : blockXY) {
					if (block != null && block instanceof ConveyorBlock) {
						((ConveyorBlock) block).drawArrow(s);
					}
				}
			}
		}
	}

	@Override
	public Iterator<Block> iterator() {
		return new Iterator<Block>() {
			
			/**
			 * The next block.
			 */
			Block next;
			
			/**
			 * Current x-coordinate we are at.
			 */
			int x = 0;
			
			/**
			 * Current y-coordinate we are at.
			 */
			int y = 0;
			
			/**
			 * Current z-coordinate we are at.
			 */
			int z = 0;
			
			{
				findNext();
			}
			
			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public Block next() {
				Block toReturn = next;
				
				if (toReturn == null) {
					throw new NoSuchElementException("No more elements in this BlockCollection");
				}
				findNext();
				
				return toReturn;
			}
			
			/**
			 * Finds the next block and puts it in <code>next</code>. If there
			 * is no next block, it puts <code>null</code> there.
			 */
			private void findNext() {
				for (; x < sizeX; x++) {
					for (; y < sizeY; y++) {
						for (; z < sizeZ; z++) {
							Block b = blocks[x][y][z];
							if (b != null && b != next) {
								next = b;
								return;
							}
						}
						z = 0;
					}
					y = 0;
				}
				x = 0;
				
				next = null;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Removing blocks is not allowed for a BlockCollection");
			}
		};
	}
}
