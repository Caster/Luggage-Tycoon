package accg.objects;

import accg.State;
import accg.simulation.Simulation;

/**
 * Any object that can be drawn in the 3D world.
 */
public abstract class DrawableObject {
	
	/**
	 * Whether this object is represented in the physics engine.
	 * 
	 * New objects need to be announced to the {@link Simulation} class, so that
	 * it can be put into the physics calculation. This flag indicates whether
	 * that has happened or not.
	 */
	public boolean inPhysics = false;
	
	/**
	 * Draws the object. You should ensure to keep the OpenGL transform the same.
	 * 
	 * @param s The {@link State} object that indicates the state of the
	 * program.
	 */
	public abstract void draw(State s);
}
