package accg.objects;

import accg.State;

/**
 * The collection of blocks to draw.
 * 
 * \todo This class at the moment is a mixture of model and view code.
 * It would be nice to split this into two classes, where this class only
 * becomes responsible for drawing the contents of the model class.
 */
public class BlockCollection extends DrawableObject {
	
	/**
	 * Array of blocks. <code>blocks[x][y][z]</code> contains the block
	 * on the coordinate (x, y, z), or <code>null</code> if there is no
	 * block there.
	 */
	private Block[][][] blocks;
	
	/**
	 * The size of this {@link BlockCollection}, in the number of blocks.
	 */
	private int size;
	
	/**
	 * Creates a new {@link BlockCollection} of the given size.
	 * 
	 * @param size The size, in number of blocks.
	 */
	public BlockCollection(int size) {
		this.blocks = new Block[size][size][size];
		this.size = size;
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
	 * Returns the block on the given coordinate.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @return The block on coordinate (x, y, z), or <code>null</code> if
	 * there is no block there.
	 */
	public Block getBlock(int x, int y, int z) {
		return blocks[x][y][z];
	}
	
	/**
	 * Returns the size of this {@link BlockCollection}.
	 * @return The size.
	 */
	public int getSize() {
		return size;
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
	}
}
