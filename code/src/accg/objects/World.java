package accg.objects;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.Block.Orientation;
import accg.objects.blocks.*;
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

		addBlock(new StraightConveyorBlock(2, 7, 16, Orientation.RIGHT));
		addBlock(new StraightConveyorBlock(3, 7, 15, Orientation.DOWN));
		addBlock(new StraightConveyorBlock(3, 6, 14, Orientation.DOWN));
		addBlock(new StraightConveyorBlock(3, 5, 13, Orientation.DOWN));
		addBlock(new StraightConveyorBlock(3, 4, 4, Orientation.LEFT));
		addBlock(new StraightConveyorBlock(2, 4, 2, Orientation.UP));
		addBlock(new StraightConveyorBlock(2, 5, 0, Orientation.RIGHT));
		addBlock(new StraightConveyorBlock(3, 5, 0, Orientation.RIGHT));
		addBlock(new AscendingConveyorBlock(4, 5, 0, Orientation.RIGHT));
		addBlock(new AscendingConveyorBlock(5, 5, 1, Orientation.RIGHT));
		addBlock(new DescendingConveyorBlock(6, 5, 1, Orientation.RIGHT));
		addBlock(new DescendingConveyorBlock(7, 5, 0, Orientation.RIGHT));
		addBlock(new StraightConveyorBlock(8, 5, 0, Orientation.RIGHT));
		
		addBlock(new StraightConveyorBlock(6, 9, 3, Orientation.UP));
		addBlock(new AscendingConveyorBlock(6, 10, 3, Orientation.UP));
		addBlock(new StraightConveyorBlock(6, 11, 3, Orientation.RIGHT));
		addBlock(new AscendingConveyorBlock(7, 11, 3, Orientation.RIGHT));
		addBlock(new StraightConveyorBlock(8, 11, 3, Orientation.DOWN));
		addBlock(new AscendingConveyorBlock(8, 10, 3, Orientation.DOWN));
		addBlock(new StraightConveyorBlock(8, 9, 3, Orientation.LEFT));
		addBlock(new AscendingConveyorBlock(7, 9, 3, Orientation.LEFT));
		
		addBlock(new BendRightConveyorBlock(11, 9, 3, Orientation.LEFT));
		addBlock(new StraightConveyorBlock(11, 10, 3, Orientation.UP));
		addBlock(new BendRightConveyorBlock(11, 11, 3, Orientation.UP));
		addBlock(new StraightConveyorBlock(12, 11, 3, Orientation.RIGHT));
		addBlock(new BendRightConveyorBlock(13, 11, 3, Orientation.RIGHT));
		addBlock(new StraightConveyorBlock(13, 10, 3, Orientation.DOWN));
		addBlock(new BendRightConveyorBlock(13, 9, 3, Orientation.DOWN));
		addBlock(new StraightConveyorBlock(12, 9, 3, Orientation.LEFT));
		
		addBlock(new BendLeftConveyorBlock(11, 4, 3, Orientation.DOWN));
		addBlock(new StraightConveyorBlock(11, 5, 3, Orientation.DOWN));
		addBlock(new BendLeftConveyorBlock(11, 6, 3, Orientation.LEFT));
		addBlock(new StraightConveyorBlock(12, 6, 3, Orientation.LEFT));
		addBlock(new BendLeftConveyorBlock(13, 6, 3, Orientation.UP));
		addBlock(new StraightConveyorBlock(13, 5, 3, Orientation.UP));
		addBlock(new BendLeftConveyorBlock(13, 4, 3, Orientation.RIGHT));
		addBlock(new StraightConveyorBlock(12, 4, 3, Orientation.RIGHT));
		addObject(bc);
		
		luggage = new Container<>();
		addObject(luggage);
	}
	
	
	/**
	 * Add a block to the {@link BlockCollection} in this {@link World}.
	 * The block will be placed at the position it indicates.
	 * 
	 * @param cb {@link ConveyorBlock} to be added.
	 * @throws NullPointerException If <code>cb == null</code>.
	 */
	public void addBlock(ConveyorBlock cb) {
		bc.setBlock(cb);
		state.simulation.addBlock(cb);
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
}
