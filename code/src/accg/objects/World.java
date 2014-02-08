package accg.objects;

import accg.objects.Block.Orientation;
import accg.objects.blocks.ConveyorBlock;

/**
 * The world that contains all other objects.
 */
public class World extends Container {
	
	BlockCollection bc;
	
	public World() {
		addObject(new Floor());
		
		// TODO add other stuff here, like walls
		
		bc = new BlockCollection(16);
		bc.setBlock(new ConveyorBlock(0, 0, 0, Orientation.RIGHT), 0, 0, 0);
		bc.setBlock(new ConveyorBlock(1, 0, 0, Orientation.RIGHT), 1, 0, 0);
		bc.setBlock(new ConveyorBlock(2, 0, 0, Orientation.RIGHT), 2, 0, 0);
		addObject(bc);
	}
}
