package accg.objects;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.blocks.*;
import accg.objects.blocks.ConveyorBlock.ConveyorBlockType;
import accg.simulation.Simulation;

/**
 * The world that contains all other objects.
 */
public class World extends Container<DrawableObject> {
	
	/**
	 * The collection of blocks in the world.
	 */
	public BlockCollection bc;
	
	/**
	 * The luggage objects.
	 */
	public Container<Luggage> luggage;
	
	/**
	 * How many blocks have been placed in this World.
	 */
	protected int blockCount;
	/**
	 * How many blocks can be built in the scene.
	 */
	protected int blockLimit;
	/**
	 * How many pieces of luggage have been lost.
	 * A piece of luggage is lost when it falls on the floor for example.
	 */
	protected int lostLuggageCount;
	
	/**
	 * State of the program, used to access {@link Simulation}.
	 */
	private State state;
	
	/**
	 * Creates a new world. It is supposed that a {@link Simulation} object has
	 * been instantiated in the given {@link State} object already. This is used
	 * to add shapes to that simulation.
	 * 
	 * @param s The state object.
	 */
	public World(State s) {
		this.state = s;
		
		addObject(new Walls());
		
		bc = new BlockCollection(s.fieldLength, s.fieldWidth, s.fieldHeight);
		addObject(bc);
		
		luggage = new Container<>();
		addObject(luggage);
		
		blockCount = 0;
		blockLimit = -1;
		lostLuggageCount = 0;
	}
	
	/**
	 * Returns the number of lost pieces of luggage.
	 * @return the number of lost pieces of luggage.
	 */
	public int getLostLuggageCount() {
		return lostLuggageCount;
	}
	
	/**
	 * Increment the count of number of lost pieces of luggage.
	 */
	public void incrementLostLuggageCount() {
		lostLuggageCount++;
	}
	
	/**
	 * Reset the count of number of lost pieces of luggage to zero.
	 */
	public void resetLostLuggageCount() {
		lostLuggageCount = 0;
	}
	
	/**
	 * Put some blocks in the scene, to start with.
	 */
	public void initialiseSomeBlocks() {
		addBlock(state, new EnterBlock(1, 1, 8, Orientation.RIGHT,
				EnterBlock.DEFAULT_TIME_BETWEEN_LUGGAGE));
		addBlock(state, new LeaveBlock(8, 1, 8, Orientation.RIGHT));
		
		addBlock(state, new FlatConveyorBlock(2, 7, 16, Orientation.RIGHT));
		addBlock(state, new FlatConveyorBlock(3, 7, 15, Orientation.DOWN));
		addBlock(state, new FlatConveyorBlock(3, 6, 14, Orientation.DOWN));
		addBlock(state, new FlatConveyorBlock(3, 5, 13, Orientation.DOWN));
		addBlock(state, new FlatConveyorBlock(3, 4, 4, Orientation.LEFT));
		addBlock(state, new FlatConveyorBlock(2, 4, 2, Orientation.UP));
		addBlock(state, new FlatConveyorBlock(2, 5, 0, Orientation.RIGHT));
		addBlock(state, new FlatConveyorBlock(3, 5, 0, Orientation.RIGHT));
		addBlock(state, new AscendingConveyorBlock(4, 5, 0, Orientation.RIGHT));
		addBlock(state, new AscendingConveyorBlock(5, 5, 1, Orientation.RIGHT));
		addBlock(state, new DescendingConveyorBlock(6, 5, 1, Orientation.RIGHT));
		addBlock(state, new DescendingConveyorBlock(7, 5, 0, Orientation.RIGHT));
		addBlock(state, new FlatConveyorBlock(8, 5, 0, Orientation.RIGHT));
		
		addBlock(state, new FlatConveyorBlock(6, 9, 3, Orientation.UP));
		addBlock(state, new AscendingConveyorBlock(6, 10, 3, Orientation.UP));
		addBlock(state, new FlatConveyorBlock(6, 11, 3, Orientation.RIGHT));
		addBlock(state, new AscendingConveyorBlock(7, 11, 3, Orientation.RIGHT));
		addBlock(state, new FlatConveyorBlock(8, 11, 3, Orientation.DOWN));
		addBlock(state, new AscendingConveyorBlock(8, 10, 3, Orientation.DOWN));
		addBlock(state, new FlatConveyorBlock(8, 9, 3, Orientation.LEFT));
		addBlock(state, new AscendingConveyorBlock(7, 9, 3, Orientation.LEFT));
		
		addBlock(state, new BendRightConveyorBlock(11, 9, 3, Orientation.LEFT));
		addBlock(state, new FlatConveyorBlock(11, 10, 3, Orientation.UP));
		addBlock(state, new BendRightConveyorBlock(11, 11, 3, Orientation.UP));
		addBlock(state, new FlatConveyorBlock(12, 11, 3, Orientation.RIGHT));
		addBlock(state, new BendRightConveyorBlock(13, 11, 3, Orientation.RIGHT));
		addBlock(state, new FlatConveyorBlock(13, 10, 3, Orientation.DOWN));
		addBlock(state, new BendRightConveyorBlock(13, 9, 3, Orientation.DOWN));
		addBlock(state, new FlatConveyorBlock(12, 9, 3, Orientation.LEFT));
		
		addBlock(state, new BendLeftConveyorBlock(11, 4, 3, Orientation.DOWN));
		addBlock(state, new FlatConveyorBlock(11, 5, 3, Orientation.DOWN));
		addBlock(state, new BendLeftConveyorBlock(11, 6, 3, Orientation.LEFT));
		addBlock(state, new FlatConveyorBlock(12, 6, 3, Orientation.LEFT));
		addBlock(state, new BendLeftConveyorBlock(13, 6, 3, Orientation.UP));
		addBlock(state, new FlatConveyorBlock(13, 5, 3, Orientation.UP));
		addBlock(state, new BendLeftConveyorBlock(13, 4, 3, Orientation.RIGHT));
		addBlock(state, new FlatConveyorBlock(12, 4, 3, Orientation.RIGHT));
	}
	
	/**
	 * Add a block to the {@link BlockCollection} in this {@link World}.
	 * The block will be placed at the position it indicates.
	 * 
	 * <p>If adding the given block would violate the block limit, nothing will
	 * happen. It is possible to place a block at the position of some other
	 * block however, at all times.
	 * 
	 * @param s State, used to look-up neighbors.
	 * @param toAdd {@link ConveyorBlock} to be added.
	 * @throws NullPointerException If <code>cb == null</code>.
	 */
	public void addBlock(State s, Block toAdd) {
		Block b;
		if ((b = bc.getBlock(toAdd.x, toAdd.y, toAdd.z)) != null) {
			b.onDestroy();
			blockCount--;
		}
		
		if (blockLimit >= 0 && blockCount + 1 > blockLimit) {
			return;
		}
		
		bc.setBlock(toAdd);
		blockCount++;
		
		if (toAdd instanceof ConveyorBlock) {
			state.simulation.addConveyorBlock(s, (ConveyorBlock) toAdd);
			
			// update neighbors
			ConveyorBlock[] neighbors = s.world.getNeighbors((ConveyorBlock) toAdd);
			for (ConveyorBlock cbn : neighbors) {
				if (cbn == null)  continue;
				
				cbn.onDestroy();
				state.simulation.addConveyorBlock(s, cbn);
			}
			
			// update block below added block (may need a ceiling now)
			int belowIndex = bc.getFirstBlockBelowHeight(toAdd.x, toAdd.y, toAdd.z);
			if (belowIndex >= 0) {
				Block belowBlock = bc.getBlock(toAdd.x, toAdd.y, belowIndex);
				if (belowBlock instanceof ConveyorBlock) {
					belowBlock.onDestroy();
					state.simulation.addConveyorBlock(s, (ConveyorBlock) belowBlock);
				}
			}
			
			// update block above added block (may need to update scaffolding)
			int aboveIndex = bc.getFirstBlockAboveHeight(toAdd.x, toAdd.y, toAdd.z);
			if (aboveIndex >= 0) {
				Block aboveBlock = bc.getBlock(toAdd.x, toAdd.y, aboveIndex);
				if (aboveBlock instanceof ConveyorBlock) {
					aboveBlock.onDestroy();
					state.simulation.addConveyorBlock(s, (ConveyorBlock) aboveBlock);
				}
			}
		}
	}

	/**
	 * Removes a block to the {@link BlockCollection} in this {@link World}.
	 * If there was no block at the specified location, nothing happens.
	 * 
	 * @param x The x-coordinate of the block to remove.
	 * @param y The y-coordinate of the block to remove.
	 * @param z The z-coordinate of the block to remove.
	 */
	public void removeBlock(int x, int y, int z) {
		
		if (bc.getBlock(x, y, z) == null) {
			return;
		}
		
		bc.getBlock(x, y, z).onDestroy();
		bc.removeBlock(x, y, z);
		
		blockCount--;
	}

	/**
	 * Returns the number of blocks in this World.
	 * @return The number of blocks that was added through the
	 *         {@link #addBlock(State, Block)} function.
	 */
	public int getBlockCount() {
		return blockCount;
	}
	
	/**
	 * Returns the number of blocks that may be placed.
	 * @return The number of blocks that may be placed.
	 */
	public int getBlockLimit() {
		return blockLimit;
	}
	
	/**
	 * Return the index of the first coordinate in the given list on which a
	 * block is positioned. If no block is occupied, the length of the given
	 * list is returned.
	 * 
	 * @param coordinates List of coordinates to check.
	 * @return Index of a coordinate, or length of given list.
	 */
	public int getFirstTakenIndex(ArrayList<Vector3f> coordinates) {
		int i, cx, cy, cz;
		Vector3f coord;
		for (i = 0; i < coordinates.size(); i++) {
			coord = coordinates.get(i);
			cx = (int) coord.x;
			cy = (int) coord.y;
			cz = (int) coord.z;
			if (bc.inBounds(cx, cy, cz) && bc.getBlock(cx, cy, cz) != null) {
				break;
			}
		}
		return i;
	}
	
	/**
	 * Return the block at the first coordinate in the given list on which a
	 * block is positioned. If no block is occupied, {@code null} is returned.
	 * Per cell, it is checked if a block is there in a fuzzy way: the height
	 * of a block is taken into account.
	 * 
	 * @param coordinates List of coordinates to check.
	 * @return Index of a coordinate, or length of given list.
	 */
	public Block getFirstTakenBlockFuzzy(ArrayList<Vector3f> coordinates) {
		int i, cx, cy, cz;
		Vector3f coord;
		Block ret;
		for (i = 0; i < coordinates.size(); i++) {
			coord = coordinates.get(i);
			cx = (int) coord.x;
			cy = (int) coord.y;
			cz = (int) coord.z;
			if (bc.inBounds(cx, cy, cz) &&
					(ret = bc.getBlockFuzzy(cx, cy, cz)) != null) {
				return ret;
			}
		}
		return null;
	}
	
	/**
	 * Return the neighbors of a block. Refer to {@link #getNeighbors(int, int, int)}
	 * for a precise definition of what a neighbor is. This method is simply a
	 * convenience method calling that method.
	 * 
	 * @param cb Block to get neighbors of.
	 * @return Result of {@link #getNeighbors(int, int, int)} with the position of
	 *         the given block as arguments.
	 */
	public ConveyorBlock[] getNeighbors(ConveyorBlock cb) {
		return getNeighbors(cb.x, cb.y, cb.z);
	}
	
	/**
	 * Return the neighbors of a block on the given position. In this case, a block
	 * is considered a neighbour of the block at that position if:
	 * <ul>
	 *   - both this block and its neighbor are ConveyorBlocks;
	 *   - their orientation matches;
	 *   - their "endpoints" match up.
	 * </ul>
	 * So basically, two blocks are neighbors if they could be drawn as one conveyor
	 * belt instead of two separate ones.
	 * 
	 * @param x The x-coordinate of the position that is queried.
	 * @param y The y-coordinate of the position that is queried.
	 * @param z The z-coordinate of the position that is queried.
	 * @return The potential neighbors of the block at given position, as described
	 *         above. The returned array is guaranteed to have two elements, of which
	 *         the elements may be {@code null}. It is also possible that this method
	 *         returns {@code null}, namely when there is no block at the given
	 *         position or the given position is invalid.
	 */
	public ConveyorBlock[] getNeighbors(int x, int y, int z) {
		// find 'this' block and check it
		ConveyorBlock cb;
		try {
			Block b = bc.getBlock(x, y, z);
			if (b == null || !(b instanceof ConveyorBlock)) {
				return null;
			}
			cb = (ConveyorBlock) b;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return null;
		}
		
		ConveyorBlock[] result = new ConveyorBlock[2];
		Vector3f pos = new Vector3f(x, y, z);
		Orientation blockOrientation = cb.getOrientation();
		Orientation blockOrientationRot;
		switch (cb.getConveyorBlockType()) {
		case BEND_LEFT:  blockOrientationRot = blockOrientation.rotateLeft(); break;
		case BEND_RIGHT: blockOrientationRot = blockOrientation.rotateRight(); break;
		default:         blockOrientationRot = blockOrientation;
		}
		
		// check first neighbor: a LeaveBlock can never have this one
		if (cb.getConveyorBlockType() == ConveyorBlockType.ASCENDING) {
			pos.z += 1;
		}
		pos = blockOrientationRot.moveFrom(pos, 1);
		if (cb.getConveyorBlockType() != ConveyorBlockType.LEAVE) {
			if (bc.inBounds((int) pos.x, (int) pos.y, (int) pos.z)) {
				Block b = bc.getBlock((int) pos.x, (int) pos.y, (int) pos.z);
				// do not draw something to the back of an EnterBlock
				if (!(b instanceof EnterBlock)) {
					if (b instanceof ConveyorBlock && !(b instanceof DescendingConveyorBlock)) {
						ConveyorBlock cbn = (ConveyorBlock) b;
						if (haveMatchingOrientations(cb, cbn)) {
							result[1] = cbn;
						}
					}
					if (result[1] == null &&
							bc.inBounds((int) pos.x, (int) pos.y, (int) pos.z - 1)) {
						b = bc.getBlock((int) pos.x, (int) pos.y, (int) pos.z - 1);
						if (b != null && b instanceof DescendingConveyorBlock) {
							ConveyorBlock cbn = (ConveyorBlock) b;
							if (haveMatchingOrientations(cb, cbn)) {
								result[1] = cbn;
							}
						}
					}
				}
			}
		}
		
		// check second neighbor: an EnterBlock can never have this one
		if (cb.getConveyorBlockType() != ConveyorBlockType.ENTER) {
			if (cb.getConveyorBlockType() == ConveyorBlockType.ASCENDING) {
				// undo transformation above
				pos.z -= 1;
			} else if (cb.getConveyorBlockType() == ConveyorBlockType.DESCENDING) {
				// block before is higher
				pos.z += 1;
			}
			pos = blockOrientation.moveFrom(blockOrientationRot.moveFrom(pos, -1), -1);
			if (bc.inBounds((int) pos.x, (int) pos.y, (int) pos.z)) {
				Block b = bc.getBlock((int) pos.x, (int) pos.y, (int) pos.z);
				// do not draw something to the back of a LeaveBlock
				if (!(b instanceof LeaveBlock)) {
					if (b instanceof ConveyorBlock && !(b instanceof AscendingConveyorBlock)) {
						ConveyorBlock cbn = (ConveyorBlock) b;
						if (haveMatchingOrientations(cbn, cb)) {
							result[0] = cbn;
						}
					}
					if (result[0] == null &&
							bc.inBounds((int) pos.x, (int) pos.y, (int) pos.z - 1)) {
						b = bc.getBlock((int) pos.x, (int) pos.y, (int) pos.z - 1);
						if (b != null && b instanceof AscendingConveyorBlock) {
							ConveyorBlock cbn = (ConveyorBlock) b;
							if (haveMatchingOrientations(cbn, cb)) {
								result[0] = cbn;
							}
						}
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Change how many blocks may be placed in this world. Note that when
	 * decreasing this value, no blocks are automatically removed if the number
	 * of blocks is violating the new limit.
	 * @param blockLimit The new limit on number of blocks that can be placed.
	 */
	public void setBlockLimit(int blockLimit) {
		this.blockLimit = blockLimit;
	}
	
	/**
	 * Return if, when going from cb1 to cb2, this is possible looking at the
	 * orientation of the blocks.
	 * 
	 * @param cb1 First block.
	 * @param cb2 Second block.
	 * @return If the two blocks 'match'.
	 */
	private boolean haveMatchingOrientations(ConveyorBlock cb1, ConveyorBlock cb2) {
		Orientation or1 = cb1.getOrientation();
		Orientation or2 = cb2.getOrientation();
		
		if (cb1 instanceof BendLeftConveyorBlock) {
			or1 = or1.rotateLeft();
		} else if (cb1 instanceof BendRightConveyorBlock) {
			or1 = or1.rotateRight();
		}
		
		return or1 == or2;
	}
}
