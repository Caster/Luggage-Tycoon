package accg.objects;

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
	 * Creates a new world.
	 */
	public World() {
		addObject(new Floor());
		
		// TODO add other stuff here, like walls
		
		bc = new BlockCollection(16);
		bc.setBlock(new StraightConveyorBlock(1, 5, 0, Orientation.DOWN), 1, 5, 0);
		bc.setBlock(new AscendingConveyorBlock(1, 4, 0, Orientation.DOWN), 1, 4, 0);
		bc.setBlock(new AscendingConveyorBlock(1, 3, 1, Orientation.DOWN), 1, 3, 1);
		bc.setBlock(new AscendingConveyorBlock(1, 2, 2, Orientation.DOWN), 1, 2, 2);
		bc.setBlock(new AscendingConveyorBlock(1, 1, 3, Orientation.DOWN), 1, 1, 3);
		bc.setBlock(new BendLeftConveyorBlock(1, 0, 4, Orientation.DOWN), 1, 0, 4);
		bc.setBlock(new DescendingConveyorBlock(2, 0, 3, Orientation.RIGHT), 2, 0, 3);
		bc.setBlock(new DescendingConveyorBlock(3, 0, 2, Orientation.RIGHT), 3, 0, 2);
		bc.setBlock(new BendLeftConveyorBlock(4, 0, 2, Orientation.RIGHT), 4, 0, 2);
		bc.setBlock(new BendLeftConveyorBlock(4, 1, 2, Orientation.UP), 4, 1, 2);
		bc.setBlock(new DescendingConveyorBlock(3, 1, 1, Orientation.LEFT), 3, 1, 1);
		bc.setBlock(new DescendingConveyorBlock(2, 1, 0, Orientation.LEFT), 2, 1, 0);
		bc.setBlock(new StraightConveyorBlock(1, 1, 0, Orientation.LEFT), 1, 1, 0);
		bc.setBlock(new BendRightConveyorBlock(0, 1, 0, Orientation.LEFT), 0, 1, 0);
		addObject(bc);
		
		luggage = new Container<>();
		addObject(luggage);
	}
}
