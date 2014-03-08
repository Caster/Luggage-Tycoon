package accg.objects;

import accg.State;

/**
 * Draws an object as a transparant "shadow" object.
 */
public class ShadowObject extends DrawableObject {
	
	private DrawableObject object;
	
	/**
	 * Creates a new shadow object with the given object.
	 * @param object The object to draw.
	 */
	public ShadowObject(DrawableObject object) {
		this.object = object;
	}

	@Override
	public void draw(State s) {
		// TODO set alpha transparency etc.
		object.draw(s);
	}
}
