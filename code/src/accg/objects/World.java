package accg.objects;

/**
 * The world that contains all other objects.
 */
public class World extends Container {
	
	public World() {
		addObject(new Floor());
		
		// TODO add more stuff here
	}
}
