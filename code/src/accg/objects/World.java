package accg.objects;

import accg.State;
import accg.objects.Block.Orientation;
import accg.objects.blocks.*;

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
	 * Creates a new world. It is supposed that a {@link Simulation} object has
	 * been instantiated in the given {@link State} object already. This is used
	 * to add shapes to that simulation.
	 * 
	 * @param s The state object.
	 */
	public World(State s) {
		this.state = s;
		
		addObject(new Floor());
		addObject(new Walls());
		
		bc = new BlockCollection(s.fieldLength, s.fieldWidth, s.fieldHeight);
	/*	bc.setBlock(new StraightConveyorBlock(1, 5, 0, Orientation.DOWN));
		bc.setBlock(new AscendingConveyorBlock(1, 4, 0, Orientation.DOWN));
		bc.setBlock(new AscendingConveyorBlock(1, 3, 1, Orientation.DOWN));
		bc.setBlock(new AscendingConveyorBlock(1, 2, 2, Orientation.DOWN));
		bc.setBlock(new AscendingConveyorBlock(1, 1, 3, Orientation.DOWN));
		bc.setBlock(new BendLeftConveyorBlock(1, 0, 4, Orientation.DOWN));
		bc.setBlock(new DescendingConveyorBlock(2, 0, 3, Orientation.RIGHT));
		bc.setBlock(new DescendingConveyorBlock(3, 0, 2, Orientation.RIGHT));
		bc.setBlock(new BendLeftConveyorBlock(4, 0, 2, Orientation.RIGHT));
		bc.setBlock(new BendLeftConveyorBlock(4, 1, 2, Orientation.UP));
		bc.setBlock(new DescendingConveyorBlock(3, 1, 1, Orientation.LEFT));
		bc.setBlock(new DescendingConveyorBlock(2, 1, 0, Orientation.LEFT));
		bc.setBlock(new StraightConveyorBlock(1, 1, 0, Orientation.LEFT));
		bc.setBlock(new BendRightConveyorBlock(0, 1, 0, Orientation.LEFT));*/

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
	
	/** State of the program, used to access {@link Simulation}. */
	private State state;
}
