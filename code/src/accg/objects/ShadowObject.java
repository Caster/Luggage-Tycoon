package accg.objects;

import static org.lwjgl.opengl.GL11.*;
import accg.State;

/**
 * Draws an object as a "shadow" object.
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
		
		glEnable(GL_BLEND);
		glColor4f(1, 1, 1, 0.50f);
		
		object.draw(s);
		
		glDisable(GL_BLEND);
	}
}
