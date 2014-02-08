package accg.objects;

import accg.State;

/**
 * Any object that can be drawn in the 3D world.
 */
public abstract class DrawableObject {
	
	/**
	 * Draws the object. You should ensure to keep the OpenGL transform the same.
	 * 
	 * @param s The {@link State} object that indicates the state of the
	 * program.
	 */
	public abstract void draw(State s);
}
