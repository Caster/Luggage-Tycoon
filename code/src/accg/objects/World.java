package accg.objects;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.Block.Orientation;
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
	}
	
	/**
	 * Put some blocks in the scene, to start with.
	 */
	public void initialiseSomeBlocks() {
		addBlock(state, new StraightConveyorBlock(2, 7, 16, Orientation.RIGHT));
		addBlock(state, new StraightConveyorBlock(3, 7, 15, Orientation.DOWN));
		addBlock(state, new StraightConveyorBlock(3, 6, 14, Orientation.DOWN));
		addBlock(state, new StraightConveyorBlock(3, 5, 13, Orientation.DOWN));
		addBlock(state, new StraightConveyorBlock(3, 4, 4, Orientation.LEFT));
		addBlock(state, new StraightConveyorBlock(2, 4, 2, Orientation.UP));
		addBlock(state, new StraightConveyorBlock(2, 5, 0, Orientation.RIGHT));
		addBlock(state, new StraightConveyorBlock(3, 5, 0, Orientation.RIGHT));
		addBlock(state, new AscendingConveyorBlock(4, 5, 0, Orientation.RIGHT));
		addBlock(state, new AscendingConveyorBlock(5, 5, 1, Orientation.RIGHT));
		addBlock(state, new DescendingConveyorBlock(6, 5, 1, Orientation.RIGHT));
		addBlock(state, new DescendingConveyorBlock(7, 5, 0, Orientation.RIGHT));
		addBlock(state, new StraightConveyorBlock(8, 5, 0, Orientation.RIGHT));
		
		addBlock(state, new StraightConveyorBlock(6, 9, 3, Orientation.UP));
		addBlock(state, new AscendingConveyorBlock(6, 10, 3, Orientation.UP));
		addBlock(state, new StraightConveyorBlock(6, 11, 3, Orientation.RIGHT));
		addBlock(state, new AscendingConveyorBlock(7, 11, 3, Orientation.RIGHT));
		addBlock(state, new StraightConveyorBlock(8, 11, 3, Orientation.DOWN));
		addBlock(state, new AscendingConveyorBlock(8, 10, 3, Orientation.DOWN));
		addBlock(state, new StraightConveyorBlock(8, 9, 3, Orientation.LEFT));
		addBlock(state, new AscendingConveyorBlock(7, 9, 3, Orientation.LEFT));
		
		addBlock(state, new BendRightConveyorBlock(11, 9, 3, Orientation.LEFT));
		addBlock(state, new StraightConveyorBlock(11, 10, 3, Orientation.UP));
		addBlock(state, new BendRightConveyorBlock(11, 11, 3, Orientation.UP));
		addBlock(state, new StraightConveyorBlock(12, 11, 3, Orientation.RIGHT));
		addBlock(state, new BendRightConveyorBlock(13, 11, 3, Orientation.RIGHT));
		addBlock(state, new StraightConveyorBlock(13, 10, 3, Orientation.DOWN));
		addBlock(state, new BendRightConveyorBlock(13, 9, 3, Orientation.DOWN));
		addBlock(state, new StraightConveyorBlock(12, 9, 3, Orientation.LEFT));
		
		addBlock(state, new BendLeftConveyorBlock(11, 4, 3, Orientation.DOWN));
		addBlock(state, new StraightConveyorBlock(11, 5, 3, Orientation.DOWN));
		addBlock(state, new BendLeftConveyorBlock(11, 6, 3, Orientation.LEFT));
		addBlock(state, new StraightConveyorBlock(12, 6, 3, Orientation.LEFT));
		addBlock(state, new BendLeftConveyorBlock(13, 6, 3, Orientation.UP));
		addBlock(state, new StraightConveyorBlock(13, 5, 3, Orientation.UP));
		addBlock(state, new BendLeftConveyorBlock(13, 4, 3, Orientation.RIGHT));
		addBlock(state, new StraightConveyorBlock(12, 4, 3, Orientation.RIGHT));
	}
	
	/**
	 * Add a block to the {@link BlockCollection} in this {@link World}.
	 * The block will be placed at the position it indicates.
	 * 
	 * @param s State, used to look-up neighbors.
	 * @param cb {@link ConveyorBlock} to be added.
	 * @throws NullPointerException If <code>cb == null</code>.
	 */
	public void addBlock(State s, ConveyorBlock cb) {
		Block b;
		if ((b = bc.getBlock(cb.x, cb.y, cb.z)) != null) {
			b.onDestroy();
		}
		bc.setBlock(cb);
		state.simulation.addBlock(s, cb);
		
		// update neighbors
		ConveyorBlock[] neighbors = s.world.getNeighbors(cb);
		for (ConveyorBlock cbn : neighbors) {
			if (cbn == null)  continue;
			
			cbn.onDestroy();
			state.simulation.addBlock(s, cbn);
		}
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
		
		// check first neighbor
		if (cb.getConveyorBlockType() == ConveyorBlockType.ASCENDING) {
			pos.z += 1;
		}
		pos = cb.getOrientation().moveFrom(pos, 1);
		Block b = bc.getBlock((int) pos.x, (int) pos.y, (int) pos.z);
		if (b instanceof ConveyorBlock) {
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
		
		// check second neighbor
		if (cb.getConveyorBlockType() == ConveyorBlockType.ASCENDING) {
			// undo transformation above
			pos.z -= 1;
		} else if (cb.getConveyorBlockType() == ConveyorBlockType.DESCENDING) {
			// block before is higher
			pos.z += 1;
		}
		pos = cb.getOrientation().moveFrom(pos, -2);
		b = bc.getBlock((int) pos.x, (int) pos.y, (int) pos.z);
		if (b instanceof ConveyorBlock) {
			ConveyorBlock cbn = (ConveyorBlock) b;
			if (haveMatchingOrientations(cbn, cb)) {
				result[0] = cbn;
			}
		}
		if (result[0] == null &&
				bc.inBounds((int) pos.x, (int) pos.y, (int) pos.z - 1)) {
			b = bc.getBlock((int) pos.x, (int) pos.y, (int) pos.z - 1);
			if (b != null && (b instanceof AscendingConveyorBlock ||
					b instanceof DescendingConveyorBlock)) {
				ConveyorBlock cbn = (ConveyorBlock) b;
				if (haveMatchingOrientations(cbn, cb)) {
					result[0] = cbn;
				}
			}
		}
		
		return result;
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
